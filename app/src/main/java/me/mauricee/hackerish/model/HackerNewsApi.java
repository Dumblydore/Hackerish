package me.mauricee.hackerish.model;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HackerNewsApi {

    @GET("v0/topstories.json")
    Flowable<List<Integer>>
    getTopStories();

    @GET("v0/newstories.json")
    Flowable<List<Integer>>
    getNewStories();

    @GET("v0/item/{id}.json")
    Single<Item> getItem(@Path("id") Integer id);

}
