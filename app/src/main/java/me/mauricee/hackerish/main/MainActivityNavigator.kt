package me.mauricee.hackerish.main

import android.net.Uri
import me.mauricee.hackerish.domain.hackerNews.Item
import me.mauricee.hackerish.model.Story

interface MainActivityNavigator {

    fun displayStoryDetails(item: Story)

    fun navigateToUrl(uri: Uri)

    fun pop()

}