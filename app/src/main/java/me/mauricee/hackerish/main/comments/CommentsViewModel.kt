package me.mauricee.hackerish.main.comments

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.mauricee.hackerish.HackerishViewModel
import me.mauricee.hackerish.domain.hackerNews.HackerNewsApi
import me.mauricee.hackerish.domain.hackerNews.Item
import javax.inject.Inject

class CommentsViewModel @Inject constructor(private val api: HackerNewsApi) : HackerishViewModel() {

    internal fun comments(selectedItem: Int): Observable<Item> {
        return api.getItem(selectedItem)
                .map(Item::kids)
                .flatMapObservable { Observable.fromIterable(it).flatMapSingle(api::getItem) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }
}