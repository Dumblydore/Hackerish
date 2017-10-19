package me.mauricee.hackerish.model

import android.net.Uri
import me.mauricee.hackerish.domain.hackerNews.Item
import org.jsoup.Jsoup
import java.util.regex.Pattern

class Story(private val item: Item, html: String?) {

    companion object {
        private val pattern = Regex("^(\\w+\\.)")

        val empty = Story(Item(-1, true, "", "", -1, "", true, -1, -1, emptyList(), "", -1, "", emptyList(), -1),"")
    }

    val favicon: Uri

    val icon: Uri

    val id = item.id

    val title = item.title

    val user = item.by

    val url = item.url

    val host: String
        get() {
            val host = if (url.host.isNullOrEmpty()) "?" else url.host
            println("Parsing host: $host")
            return if (host.toCharArray().filter { '.' == it }.count() >= 2)
                host.replace(pattern, "")
            else
                host.toString()
        }

    val comments
        get() = item.kids ?: emptyList()

    val hasComments
        get() = item.kids?.isNotEmpty() ?: false

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
}