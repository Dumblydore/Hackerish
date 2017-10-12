package me.mauricee.hackerish.main

import me.mauricee.hackerish.domain.hackerNews.Item
import me.mauricee.hackerish.model.Story

interface MainActivityNavigator {

    fun displayStoryDetails(item: Story)

    fun pop()

}