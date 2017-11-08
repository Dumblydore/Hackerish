package me.mauricee.hackerish.model

import me.mauricee.hackerish.domain.hackerNews.Item
import org.threeten.bp.Instant

open class Post(item: Item) {

    val id = item.id

    val author = item.by

    val date = Instant.ofEpochSecond(item.time)

    val isDeleted = item.deleted
}