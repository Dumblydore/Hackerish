package me.mauricee.hackerish.model

import io.reactivex.Observable
import me.mauricee.hackerish.domain.hackerNews.Item


class Comment(private val item: Item, val replies: Observable<Comment>) {

    val hasReplies
        get() = item.kids?.isNotEmpty() ?: false

    val text
        get() = item.text

    val author
        get() = item.by
}
