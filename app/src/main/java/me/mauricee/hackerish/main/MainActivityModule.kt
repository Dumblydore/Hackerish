package me.mauricee.hackerish.main

import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    fun provideNavigator(activity: MainActivity): MainActivityNavigator = activity

    @Provides
    fun selectedItem(activity: MainActivity) = activity.selectedItem

}