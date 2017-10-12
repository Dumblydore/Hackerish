package me.mauricee.hackerish.rx

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class ResponseCallbackObservable(private val call: Call) : Observable<Response>() {

    override fun subscribeActual(observer: Observer<in Response>) {
        val listener = Listener(observer, call)

        observer.onSubscribe(listener)
        call.enqueue(listener)
    }


    inner class Listener(private val observer: Observer<in Response>, private val call: Call) : MainThreadDisposable(), Callback {

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                observer.onNext(response)
                observer.onComplete()
            } else
                observer.onError(Exception("Http code: ${response.code()}"))
        }

        override fun onFailure(call: Call, e: IOException) {
            observer.onError(e)
        }

        override fun onDispose() {
            call.cancel()
        }

    }

}