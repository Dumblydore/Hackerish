package me.mauricee.hackerish.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import me.mauricee.hackerish.domain.hackerNews.Item
import org.jsoup.Jsoup

class Story(item: Item, html: String?) : Post(item), Parcelable {

    val favicon: Uri

    val icon: Uri

    val title = item.title

    val user = item.by

    val text = item.text ?: ""

    val url = item.url

    val comments = item.kids ?: emptyList()

    val hasComments = item.kids?.isNotEmpty() ?: false

    val host: String
        get() {
            val host = if (url.host.isNullOrEmpty()) "?" else url.host
            println("Parsing host: $host")
            return if (host.toCharArray().filter { '.' == it }.count() >= 2)
                host.replace(pattern, "")
            else
                host.toString()
        }


    //TODO clean this up!
    init {
        if (!html.isNullOrEmpty()) {
            val doc = Jsoup.parse(html)
            icon = doc.getElementsByTag("meta")
                    .filter { (it.attr("property").toString()).equals("og:image", true) }
                    .map { Uri.parse(it.attr("content")) }
                    .firstOrNull() ?: Uri.EMPTY

            val e = doc.head().select("link[href~=.*\\.(ico|png)]").firstOrNull()
            val faviconUrl = e?.attr("href") ?: ""
            favicon = if (faviconUrl.isEmpty()) Uri.EMPTY else Uri.parse(faviconUrl)

        } else {
            favicon = Uri.EMPTY
            icon = Uri.EMPTY
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(favicon.toString())
        dest.writeString(icon.toString())
        dest.writeInt(id)
        dest.writeString(title)
        dest.writeString(user)
        dest.writeString(url.toString())
        dest.writeString(text)
        dest.writeIntArray(comments.toIntArray())
    }

    override fun describeContents(): Int {
        return favicon.hashCode() +
                icon.hashCode() +
                id.hashCode() +
                title.hashCode() +
                user.hashCode() +
                url.hashCode() +
                text.hashCode() +
                comments.hashCode() +
                hasComments.hashCode()
    }

    companion object {
        private val pattern = Regex("^(\\w+\\.)")

        val empty = Story(Item(-1, true, "", "", -1, "", true, -1, -1, emptyList(), "", -1, "", emptyList(), -1), "")

        val CREATOR = object : Parcelable.Creator<Story> {
            override fun newArray(size: Int): Array<Story> = newArray(size)
            override fun createFromParcel(source: Parcel): Story {
                val favicon = source.readString()
                val icon = source.readString()
                val id = source.readInt()
                val title = source.readString()
                val user = source.readString()
                val url = source.readString()
                val text = source.readString()

                val comments = source.createIntArray()
                source.readIntArray(comments)

                val item = Item(id, false, "story", user, 0, text,
                        false, 0, 0, comments.toList(), url,
                        0, title, emptyList(), comments.size)
                return Story(item, null)
            }
        }
    }
}