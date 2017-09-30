package me.mauricee.hackerish

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleRegistry
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable

abstract class HackerishActivity : DaggerAppCompatActivity() {

    internal val subscriptions = CompositeDisposable()
    private var lifecycleRegistry: LifecycleRegistry? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleRegistry = LifecycleRegistry(this)
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry!!
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleRegistry = null
    }
}
