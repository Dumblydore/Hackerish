package me.mauricee.hackerish.domain.hackerNews

import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface HackerNewsApi {

    @get:GET("v0/topstories.json")
    val topStories: Flowable<List<Int>>

    @get:GET("v0/newstories.json")
    val newStories: Flowable<List<Int>>

    @get:GET("v0/beststories.json")
    val bestStories: Flowable<List<Int>>

    @get:GET("v0/askstories.json")
    val askStories: Flowable<List<Int>>

    @get:GET("v0/showstories.json")
    val showStories: Flowable<List<Int>>

    @get:GET("v0/jobstories.json")
    val jobStories: Flowable<List<Int>>


    @GET("v0/item/{id}.json")
    fun getItem(@Path("id") id: Int): Single<Item>

}
