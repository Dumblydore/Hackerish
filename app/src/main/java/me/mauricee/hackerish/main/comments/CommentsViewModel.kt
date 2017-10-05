package me.mauricee.hackerish.main.comments

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.mauricee.hackerish.HackerishViewModel
import me.mauricee.hackerish.domain.hackerNews.HackerNewsApi
import me.mauricee.hackerish.domain.hackerNews.Item
import me.mauricee.hackerish.model.Comment
import javax.inject.Inject

class CommentsViewModel @Inject constructor(private val api: HackerNewsApi) : HackerishViewModel() {

    internal fun comments(selectedItem: Int): Observable<Comment> {

        Log.d(javaClass.simpleName, "Getting comments for $selectedItem")
        return api.getItem(selectedItem)
                .map(Item::kids)
                .flatMapObservable(this::loadComments)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun loadComments(kids: List<Int>): Observable<Comment> {
        return Observable.fromIterable(kids).flatMapSingle(api::getItem)
                .map { Comment(it, loadComments(it.kids ?: emptyList())) }
                .subscribeOn(Schedulers.io())
    }
}