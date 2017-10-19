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

    @Inject internal lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject internal lateinit var viewModelFactory: ViewModelProvider.Factory

    internal lateinit var viewModel: VM
    internal val subscriptions = CompositeDisposable()
    internal val picasso = Picasso.with(activity)

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        retainInstance = true
        super.onAttach(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.clear()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return childFragmentInjector
    }

}