package me.mauricee.hackerish.model

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class Item(
        @SerializedName("id") var id: Int,
        @SerializedName("deleted") val deleted: Boolean,
        @SerializedName("type") val typeString: String,
        @SerializedName("by") val by: String,
        @SerializedName("time") val time: Int,
        @SerializedName("text") val text: String,
        @SerializedName("dead") val dead: Boolean,
        @SerializedName("parent") val parent: Int,
        @SerializedName("poll") val poll: Int,
        @SerializedName("kids") val kids: List<Int>,
        @SerializedName("url") val urlString: String?,
        @SerializedName("score") val score: Int,
        @SerializedName("title") val title: String,
        @SerializedName("parts") val parts: List<Int>,
        @SerializedName("descendants") val descendants: List<Int>
) {
    val url
        get() = if (urlString != null) Uri.parse(urlString) else Uri.EMPTY

    val type: Type
        get() {
            return when (typeString) {
                "job" -> Type.job
                "story" -> Type.story
                "comment" -> Type.comment
                "poll" -> Type.poll
                "pollopt" -> Type.pollopt
                else -> Type.job
            }
        }

    enum class Type {
        job,
        story,
        comment,
        poll,
        pollopt
    }

    lateinit var icon: Uri
}