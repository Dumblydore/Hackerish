package me.mauricee.hackerish.model

import android.net.Uri
import android.util.Log
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject
import me.mauricee.hackerish.domain.hackerNews.HackerNewsApi
import me.mauricee.hackerish.domain.hackerNews.Item
import me.mauricee.hackerish.rx.ResponseCallbackObservable
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import javax.inject.Inject

class HackerNewsManager @Inject constructor(private val api: HackerNewsApi,
                                            private val client: OkHttpClient) {

    fun topStories(): Flowable<Story> {
        return api.topStories.flatMapIterable { it }
                .flatMapSingle(this::getStory)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }

    fun newStories(): Flowable<Story> {
        return api.newStories.flatMapIterable { it }
                .flatMapSingle(this::getStory)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }

    fun comments(id: Int): Flowable<Comment> {
        return api.getItem(id)
                .map(Item::kids)
                .flatMapObservable(this::loadComments)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toFlowable(BackpressureStrategy.LATEST)
    }

    private fun loadComments(kids: List<Int>): Observable<Comment> {
        val replyStream = ReplaySubject.create<Comment>()

        return Observable.fromIterable(kids)
                .flatMapSingle(api::getItem)
                .map {
                    loadComments(it.kids ?: emptyList()).subscribe(replyStream::onNext)
                    Comment(it, replyStream.observeOn(AndroidSchedulers.mainThread())
                            .filter { f -> it.kids?.contains(f.id) ?: false })
                }
                .subscribeOn(Schedulers.io())
                .doOnDispose(replyStream::onComplete)
    }

    private fun getStory(id: Int): Single<Story> {
        return api.getItem(id).flatMap { item ->
            getIconFromUrl(item.url).map { Story(item, it) }
        }
    }

    private fun getIconFromUrl(url: Uri): Single<Uri> {
        return if (url == Uri.EMPTY) Single.just(url) else
            ResponseCallbackObservable(client.newCall(Request.Builder().url(url.toString()).build()))
                    .observeOn(Schedulers.computation())
                    .subscribeOn(Schedulers.io())
                    .map { it.body()!!.string() }
                    .map(Jsoup::parse)
                    .flatMapIterable { it.getElementsByTag("meta") }
                    .filter { (it.attr("property").toString()).equals("og:image", true) }
                    .map { Uri.parse(it.attr("content")) }
                    .onErrorReturnItem(Uri.EMPTY)
                    .doOnNext { Log.d(javaClass.simpleName, "url: $url icon: $it") }
                    .first(Uri.EMPTY)
    }

}