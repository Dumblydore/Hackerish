package me.mauricee.hackerish.inject

import android.app.Application
import android.content.Context
import com.nytimes.android.external.store3.base.impl.Store
import com.nytimes.android.external.store3.base.impl.StoreBuilder
import dagger.Module
import dagger.Provides
import me.mauricee.hackerish.model.HackerNewsApi
import me.mauricee.hackerish.model.Item
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
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
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
    fun provideHackerNewsStore(hackerNewsApi: HackerNewsApi) : Store<Item, Int>{
        return StoreBuilder.key<Int, Item>()
                .fetcher{ hackerNewsApi.getItem(it)}
                .open()
    }


}