package me.mauricee.hackerish.model

import me.mauricee.hackerish.domain.hackerNews.Item


class Comment(item: Item, val depth: Int = 0) : Post(item) {

    val text = item.text

}
