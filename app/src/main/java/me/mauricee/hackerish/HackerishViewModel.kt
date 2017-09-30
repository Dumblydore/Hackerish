package me.mauricee.hackerish

import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable

open class HackerishViewModel : ViewModel() {
    val subscriptions = CompositeDisposable()

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}