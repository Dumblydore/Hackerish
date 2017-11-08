package me.mauricee.hackerish.domain.hackerNews

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class Item(@SerializedName("id") var id: Int,
                @SerializedName("deleted") val deleted: Boolean,
                @SerializedName("type") private val typeString: String,
                @SerializedName("by") val by: String,
                @SerializedName("time") val time: Long,
                @SerializedName("text") val text: String?,
                @SerializedName("dead") val dead: Boolean,
                @SerializedName("parent") val parent: Int,
                @SerializedName("Poll") val poll: Int,
                @SerializedName("kids") val kids: List<Int>?,
                @SerializedName("url") private val urlString: String?,
                @SerializedName("score") val score: Int,
                @SerializedName("title") val title: String,
                @SerializedName("parts") val parts: List<Int>,
                @SerializedName("descendants") val descendants: Int) {

    val url
        get() = if (urlString != null) Uri.parse(urlString) else Uri.EMPTY

    val type: Type
        get() {
            return when (typeString) {
                "job" -> Type.Job
                "story" -> Type.Story
                "comment" -> Type.Comment
                "poll" -> Type.Poll
                "pollopt" -> Type.Pollopt
                else -> Type.Empty
            }
        }

    enum class Type {
        Job,
        Story,
        Comment,
        Poll,
        Pollopt,
        Empty
    }
}