package me.mauricee.hackerish.model

import io.reactivex.Observable
import me.mauricee.hackerish.domain.hackerNews.Item


data class Comment(private val item: Item, val replies: Observable<Comment>) {

    val id = item.id

    val hasReplies = item.kids?.isNotEmpty() ?: false

    val text = item.text

    val author = item.by

    val score = item.score

    val isDeleted = item.deleted
}
