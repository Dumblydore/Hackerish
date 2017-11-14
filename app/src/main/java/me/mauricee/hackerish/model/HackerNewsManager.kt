package me.mauricee.hackerish.model

import android.net.Uri
import android.util.Log
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import me.mauricee.hackerish.R.id.favicon
import me.mauricee.hackerish.common.ImageDownloader
import me.mauricee.hackerish.common.logTag
import me.mauricee.hackerish.domain.hackerNews.HackerNewsApi
import me.mauricee.hackerish.domain.hackerNews.Item
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
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
                .flatMapObservable { loadComments(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toFlowable(BackpressureStrategy.LATEST)
    }


    private fun getStory(id: Int): Single<Story> {
        return api.getItem(id).flatMap {
            getBodyFromUri(it.url)
                    .flatMap { html ->
                        Single.fromCallable { buildStory(it, html) }
                                .subscribeOn(Schedulers.computation())
                    }
        }.onErrorReturnItem(Story.empty)
    }

    private fun prepareStream(upstream: Flowable<Int>): Flowable<Story> {
        return upstream
                .flatMapSingle(this::getStory)
                .filter { Story.empty != it }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }

    private fun loadComments(kids: List<Int>, depth: Int = 0): Observable<Comment> {
        val replyStream = ReplaySubject.create<Comment>()

        return Observable.fromIterable(kids)
                .flatMapSingle(api::getItem)
                .map {
                    loadComments(it.kids ?: emptyList(), depth + 1)
                            .subscribe(replyStream::onNext)
                    Comment(it, replyStream.observeOn(AndroidSchedulers.mainThread())
                            .filter { f -> it.kids?.contains(f.id) ?: false }, depth)
                }
                .subscribeOn(Schedulers.io())
                .doOnDispose(replyStream::onComplete)
    }

    private fun getBodyFromUri(url: Uri): Single<String> {
        return if (url == Uri.EMPTY) Single.just("") else
            Single.defer {
                val a = client.newCall(Request.Builder().url(url.toString()).build())
                val body = a.execute().use {
                    it.body()?.string() ?: ""
                }
                Single.just(body)
            }.subscribeOn(Schedulers.io()).onErrorReturnItem("")
    }

    private fun buildStory(item: Item, html: String?): Story {
        var favicon: Uri = Uri.EMPTY
        var icon: Uri = Uri.EMPTY

        if (!html.isNullOrEmpty()) {
            val doc = Jsoup.parse(html)
            icon = doc.getElementsByTag("meta")
                    .filter { (it.attr("property").toString()).equals("og:image", true) }
                    .map { Uri.parse(it.attr("content")) }
                    .firstOrNull() ?: Uri.EMPTY

            val e = doc.head().select("link[href~=.*\\.(ico|png)]").firstOrNull()
            val faviconUrl = e?.attr("href") ?: ""
            favicon = if (faviconUrl.isEmpty()) Uri.EMPTY else Uri.parse(faviconUrl)

        }

        return Story(item, favicon, icon)
    }

}