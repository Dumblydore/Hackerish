package me.mauricee.hackerish.inject

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import me.mauricee.hackerish.domain.hackerNews.HackerNewsApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().disableHtmlEscaping().create()
    }

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
    fun provideHackerNewsApi(retrofit: Retrofit): HackerNewsApi {
        return retrofit.create(HackerNewsApi::class.java)
    }

    @Provides
    @Singleton
    fun providePicasso(client: OkHttpClient, context: Context): Picasso {
        return Picasso.Builder(context).downloader(OkHttp3Downloader(client))
                .build()
    }
}