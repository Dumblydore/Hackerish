package me.mauricee.hackerish.main

import me.mauricee.hackerish.model.Item

interface MainActivityNavigator {

    fun displayItemDetails(item: Item)

    fun pop()

}