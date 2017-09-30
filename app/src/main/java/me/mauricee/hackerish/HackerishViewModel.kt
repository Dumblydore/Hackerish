package me.mauricee.hackerish

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class HackerishViewModel : ViewModel() {
    val subscriptions = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}