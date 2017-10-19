package me.mauricee.hackerish

import com.squareup.picasso.Picasso
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import me.mauricee.hackerish.inject.DaggerAppComponent
import javax.inject.Inject

class HackerishApp : DaggerApplication() {

    @Inject
    lateinit var picasso: Picasso

    override fun onCreate() {
        super.onCreate()
        Picasso.setSingletonInstance(picasso)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }

}