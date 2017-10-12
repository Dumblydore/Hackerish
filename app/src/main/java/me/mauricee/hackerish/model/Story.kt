package me.mauricee.hackerish.model

import android.net.Uri
import me.mauricee.hackerish.domain.hackerNews.Item

class Story(private val item: Item, val icon: Uri) {

    val id = item.id

    val title = item.title

    val user = item.by

    val url = item.url

    val comments
        get() = item.kids ?: emptyList()

    val hasComments
        get() = item.kids?.isNotEmpty() ?: false
}