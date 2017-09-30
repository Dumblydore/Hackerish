package me.mauricee.hackerish.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HackerNewsManager @Inject constructor(private val hackerNewsApi: HackerNewsApi) {
    
}