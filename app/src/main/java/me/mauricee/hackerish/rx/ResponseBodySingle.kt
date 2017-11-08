package me.mauricee.hackerish.rx

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class ResponseBodySingle(private val call: Call) : Observable<String>() {

    override fun subscribeActual(observer: Observer<in String>) {
        val listener = Listener(observer, call)

        observer.onSubscribe(listener)
        call.enqueue(listener)
    }


    inner class Listener(private val observer: Observer<in String>,
                         private val call: Call) : MainThreadDisposable(), Callback {

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (it.isSuccessful) {
                    observer.onNext(it.body()?.string() ?: "")
                } else
                    observer.onError(Exception("Http code: ${it.code()}"))
            }
        }

        override fun onFailure(call: Call, e: IOException) {
            observer.onError(e)
        }

        override fun onDispose() {
            call.cancel()
        }

    }

}