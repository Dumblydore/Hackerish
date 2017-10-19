package me.mauricee.hackerish.model

import io.reactivex.Observable
import me.mauricee.hackerish.domain.hackerNews.Item
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime


data class Comment(private val item: Item, val replies: Observable<Comment>) {

    val id = item.id

    val hasReplies = item.kids?.isNotEmpty() ?: false

    val text = item.text

    val author = item.by

    val date = Instant.ofEpochMilli(item.time)

    val isDeleted = item.deleted
}
