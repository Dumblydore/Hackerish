package me.mauricee.hackerish.model

import me.mauricee.hackerish.domain.hackerNews.Item


data class Comment(val item: Item, val replies: Observable<Comment>)
