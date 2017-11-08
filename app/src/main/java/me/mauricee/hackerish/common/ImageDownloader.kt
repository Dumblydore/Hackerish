package me.mauricee.hackerish.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.squareup.picasso.Transformation
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Callable
import javax.inject.Inject


class ImageDownloader(private val context: Context, private val url: Uri) {

    companion object {
        val faviconPath = "${Environment.getExternalStorageDirectory().path}/images"
    }

    fun saveImageFromString(uri: String): Single<String> {
        return saveImageFromUrl(Uri.parse(uri)).map(Uri::toString)
    }

    /** Downloads image and returns url for cached image or original url if downloading fails.*/
    fun saveImageFromUrl(uri: Uri): Single<Uri> {
        return Single.create { it: SingleEmitter<Uri> ->
            Log.e(logTag, "validating uri ${uri}")
            File(faviconPath).mkdirs()
            val file = File("$faviconPath/${uri.host}-${uri.path}")
            if (file.exists()) it.onSuccess(Uri.fromFile(file))
            else {
                Picasso.with(context).load(uri).into(SaveListener(file, it))
            }
        }.map { if (it == Uri.EMPTY) uri else it }
                .doOnError { Log.e(logTag, "Failed at downloading!", it) }
    }

    class SaveListener(val saveLocation: File, val source: SingleEmitter<in Uri>) : Target {
        override fun onPrepareLoad(placeHolderDrawable: Drawable) {
            source.onSuccess(Uri.EMPTY)
        }

        override fun onBitmapFailed(errorDrawable: Drawable) {
            source.onSuccess(Uri.EMPTY)
        }

        override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
            saveLocation.createNewFile()
            val stream = FileOutputStream(saveLocation)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            stream.flush()
            stream.close()
            source.onSuccess(Uri.fromFile(saveLocation))
        }
    }
}