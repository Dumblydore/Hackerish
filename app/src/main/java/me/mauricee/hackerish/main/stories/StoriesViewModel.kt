package me.mauricee.hackerish.main.stories

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.mauricee.hackerish.HackerishViewModel
import me.mauricee.hackerish.main.MainActivityNavigator
import me.mauricee.hackerish.model.HackerNewsApi
import me.mauricee.hackerish.model.Item
import javax.inject.Inject

internal class StoriesViewModel @Inject constructor(private val api: HackerNewsApi, private val navigator: MainActivityNavigator) : HackerishViewModel() {

    var topStories: Flowable<Item>
        private set

    var newStories: Flowable<Item>
        private set


    fun select(item: Item) = navigator.displayItemDetails(item)

    init {
        topStories = api.topStories.flatMapIterable { it }
                .flatMapSingle { api.getItem(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .cache()

        newStories = api.newStories.flatMapIterable { it }
                .flatMapSingle { api.getItem(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .cache()
    }

}