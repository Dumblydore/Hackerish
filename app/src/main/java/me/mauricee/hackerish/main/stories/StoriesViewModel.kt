package me.mauricee.hackerish.main.stories

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import me.mauricee.hackerish.HackerishViewModel
import me.mauricee.hackerish.ext.put
import me.mauricee.hackerish.main.MainActivityNavigator
import me.mauricee.hackerish.model.HackerNewsApi
import me.mauricee.hackerish.model.Item
import javax.inject.Inject

internal class StoriesViewModel @Inject constructor(private val api: HackerNewsApi, private val navigator: MainActivityNavigator) : HackerishViewModel() {

    private val itemsSubject: ReplaySubject<Item> = ReplaySubject.create()

    val items: Observable<Item>
        get() = itemsSubject

    fun select(item: Item) = navigator.displayItemDetails(item)

    init {
        api.topStories.flatMapIterable { it }
                .flatMapSingle { api.getItem(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(itemsSubject::onNext, {})
                .put(subscriptions)
    }

}