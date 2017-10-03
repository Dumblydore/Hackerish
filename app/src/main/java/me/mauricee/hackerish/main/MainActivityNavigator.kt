package me.mauricee.hackerish.main

import me.mauricee.hackerish.domain.hackerNews.Item

interface MainActivityNavigator {

    fun displayItemDetails(item: Item)

    fun pop()

}