package me.mauricee.hackerish.model

import io.reactivex.Observable
import me.mauricee.hackerish.domain.hackerNews.Item
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime


class Comment(item: Item,
              val replies: Observable<Comment>,
              val depth: Int = 0) : Post(item) {

    val text = item.text

    val hasReplies = item.descendants > 0

}
