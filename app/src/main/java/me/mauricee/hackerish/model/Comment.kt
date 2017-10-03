package me.mauricee.hackerish.model


data class Comment(val item: Item, val replies: Observable<Comment>)
