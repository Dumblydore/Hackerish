package me.mauricee.hackerish

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import me.mauricee.hackerish.inject.DaggerAppComponent

class HackerishApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }

}