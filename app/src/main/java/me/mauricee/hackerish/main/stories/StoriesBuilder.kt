package me.mauricee.hackerish.main.stories

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import me.mauricee.hackerish.inject.ViewModelKey

@Module
internal abstract class StoriesBuilder {

    @ContributesAndroidInjector
    internal abstract fun StoriesFragment(): StoriesFragment

    @Binds
    @IntoMap
    @ViewModelKey(StoriesViewModel::class)
    abstract fun bindStoriesViewModel(viewModel: StoriesViewModel): ViewModel

}