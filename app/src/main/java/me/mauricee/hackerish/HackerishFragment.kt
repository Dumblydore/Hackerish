package me.mauricee.hackerish

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.support.v4.app.Fragment
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import javax.inject.Inject

abstract class HackerishFragment<VM : ViewModel> : Fragment(), HasSupportFragmentInjector {

    internal val subscriptions = CompositeDisposable()
    @Inject lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>
    protected lateinit var viewModel: VM
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var picasso: Picasso

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        retainInstance = true
        super.onAttach(context)
    }


    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return childFragmentInjector
    }

}