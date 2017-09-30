package me.mauricee.hackerish.ext

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.put(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}