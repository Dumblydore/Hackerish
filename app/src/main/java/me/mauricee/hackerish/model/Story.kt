package me.mauricee.hackerish.model

import android.net.Uri
import me.mauricee.hackerish.R.id.favicon
import me.mauricee.hackerish.domain.hackerNews.Item
import org.jsoup.Jsoup

class Story(private val item: Item, html: String?) {

    val favicon: Uri

    val icon: Uri

    val id = item.id

    val title = item.title

    val user = item.by

    val url = item.url

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