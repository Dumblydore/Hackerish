@file:JvmName("RxExt")
package me.mauricee.hackerish.rx


import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.put(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

