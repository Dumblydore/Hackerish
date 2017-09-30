package me.mauricee.hackerish.main.comments

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import me.mauricee.hackerish.inject.ViewModelKey

@Module
internal abstract class CommentsBuilder {

    @ContributesAndroidInjector
    internal abstract fun CommentsFragment(): CommentsFragment

    @Binds
    @IntoMap
    @ViewModelKey(CommentsViewModel::class)
    abstract fun bindCommentsViewModel(viewModel: CommentsViewModel): ViewModel
}