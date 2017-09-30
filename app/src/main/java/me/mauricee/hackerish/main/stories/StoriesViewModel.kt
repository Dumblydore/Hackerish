package me.mauricee.hackerish.main.stories

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject
import me.mauricee.hackerish.HackerishViewModel
import me.mauricee.hackerish.ext.put
import me.mauricee.hackerish.model.HackerNewsApi
import me.mauricee.hackerish.model.Item
import javax.inject.Inject

internal class StoriesViewModel @Inject constructor(private val api: HackerNewsApi) : HackerishViewModel() {

    private val selectedSubject: BehaviorSubject<Item> = BehaviorSubject.create()
    private val itemsSubject: ReplaySubject<Item> = ReplaySubject.create()

    val items: Observable<Item>
        get() = itemsSubject

    val selected: Observable<Item>
        get() = selectedSubject

    fun select(item: Item) {
        selectedSubject.onNext(item)
    }

    init {
        api.newStories.flatMapIterable { it }
                .flatMapSingle { api.getItem(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(itemsSubject::onNext, {})
                .put(subscriptions)
    }

}