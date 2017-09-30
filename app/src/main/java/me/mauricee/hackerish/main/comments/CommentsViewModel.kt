package me.mauricee.hackerish.main.comments

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import me.mauricee.hackerish.HackerishViewModel
import me.mauricee.hackerish.ext.put
import me.mauricee.hackerish.model.HackerNewsApi
import me.mauricee.hackerish.model.Item
import javax.inject.Inject

class CommentsViewModel @Inject constructor(selectedItem: Int, api: HackerNewsApi) : HackerishViewModel() {

    val comments: Observable<Item>
        get() = commentStream

    private val commentStream = ReplaySubject.create<Item>()

    init {
        Log.d(javaClass.simpleName, "selected item $selectedItem")
        api.getItem(selectedItem)
                .map(Item::kids)
                .flatMapObservable { Observable.fromIterable(it).flatMapSingle(api::getItem) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(commentStream::onNext, {Log.e(javaClass.simpleName, "", it)})
                .put(subscriptions)
    }
}