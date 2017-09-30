package me.mauricee.hackerish.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.mauricee.hackerish.main.comments.CommentsBuilder
import me.mauricee.hackerish.main.stories.StoriesBuilder

@Module
internal abstract class MainActivityBuilder {

    @ContributesAndroidInjector(modules = arrayOf(MainActivityModule::class, StoriesBuilder::class, CommentsBuilder::class))
    internal abstract fun mainActivity(): MainActivity
}