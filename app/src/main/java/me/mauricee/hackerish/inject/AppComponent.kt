package me.mauricee.hackerish.inject

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import me.mauricee.hackerish.HackerishApp
import me.mauricee.hackerish.main.MainActivityBuilder
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AndroidSupportInjectionModule::class, AppModule::class, ViewModelBuilder::class, MainActivityBuilder::class))
interface AppComponent : AndroidInjector<HackerishApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<HackerishApp>()

}