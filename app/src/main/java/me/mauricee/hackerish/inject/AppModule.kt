package me.mauricee.hackerish.inject

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import me.mauricee.hackerish.HackerishApp
import me.mauricee.hackerish.domain.hackerNews.HackerNewsApi
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext(application: HackerishApp): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideCache(context: Context): Cache {
        val httpCacheDirectory = File(context.cacheDir, "cache")
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        return Cache(httpCacheDirectory, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
                .cache(cache)
                .build()
    }

    @Provides
    @Singleton
    fun providePicasso(client: OkHttpClient, context: Context): Picasso {
        return Picasso.Builder(context).downloader(OkHttp3Downloader(client))
                .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().disableHtmlEscaping().create()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .baseUrl("https://hacker-news.firebaseio.com")
                .build()
    }

    @Provides
    @Singleton
    fun provideHackerNewsApi(retrofit: Retrofit): HackerNewsApi =
            retrofit.create(HackerNewsApi::class.java)
}