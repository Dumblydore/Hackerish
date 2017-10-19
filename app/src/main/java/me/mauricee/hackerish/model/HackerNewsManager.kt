package me.mauricee.hackerish.model

import android.net.Uri
import android.util.Log
import io.reactivex.*
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
import org.jsoup.nodes.Document
import java.util.regex.Pattern
import javax.inject.Inject

class HackerNewsManager @Inject constructor(private val api: HackerNewsApi,
                                            private val client: OkHttpClient) {

    fun topStories(): Flowable<Story> {
        return api.topStories.flatMapIterable { it }
                .compose(this::prepareStream)
    }

    fun newStories(): Flowable<Story> {
        return api.newStories.flatMapIterable { it }
                .compose(this::prepareStream)
    }

    fun commentsFor(id: Int): Flowable<Comment> {
        return api.getItem(id)
                .map(Item::kids)
                .flatMapObservable(this::loadComments)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toFlowable(BackpressureStrategy.LATEST)
    }

    private fun prepareStream(upstream: Flowable<Int>): Flowable<Story> {
        return upstream.flatMapSingle { getStory(it) }
                .filter { Story.empty != it }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
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
        return api.getItem(id).flatMap { getIconFromUrl(it.url).map { html -> Story(it, html) } }
                .onErrorReturnItem(Story.empty)
    }

    private fun getIconFromUrl(url: Uri): Single<String> {
        return if (url == Uri.EMPTY) Single.just("") else
            ResponseCallbackObservable(client.newCall(Request.Builder().url(url.toString()).build()))
                    .observeOn(Schedulers.computation())
                    .subscribeOn(Schedulers.io())
                    .map { it.body()!!.string() }
                    .onErrorReturnItem("")
                    .first("")
    }

}