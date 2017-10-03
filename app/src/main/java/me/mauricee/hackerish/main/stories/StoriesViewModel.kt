package me.mauricee.hackerish.main.stories

import android.net.Uri
import android.util.Log
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.mauricee.hackerish.HackerishViewModel
import me.mauricee.hackerish.main.MainActivityNavigator
import me.mauricee.hackerish.domain.hackerNews.HackerNewsApi
import me.mauricee.hackerish.domain.hackerNews.Item
import me.mauricee.hackerish.util.RxResponseCallback
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import javax.inject.Inject

internal class StoriesViewModel @Inject constructor(private val api: HackerNewsApi,
                                                    private val okHttpClient: OkHttpClient,
                                                    private val navigator: MainActivityNavigator) : HackerishViewModel() {

    var topStories: Flowable<Item>
        private set

    var newStories: Flowable<Item>
        private set


    fun select(item: Item) = navigator.displayItemDetails(item)

    init {
        topStories = api.topStories.flatMapIterable { it }
                .flatMapSingle(this::getItem)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .cache()

        newStories = api.newStories.flatMapIterable { it }
                .flatMapSingle(this::getItem)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .cache()
    }

    private fun getItem(id: Int): Single<Item> {
        return api.getItem(id).flatMap { item ->
            getIconFromUrl(item.url)
                    .doOnSuccess {
                        item.icon = it
                    }
                    .map { item }
        }
    }

    private fun getIconFromUrl(url: Uri): Single<Uri> {
        return if (url == Uri.EMPTY) Single.just(url) else
            RxResponseCallback(okHttpClient.newCall(Request.Builder().url(url.toString()).build()))
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