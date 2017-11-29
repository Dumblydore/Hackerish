package me.mauricee.hackerish.main.stories

import android.net.Uri
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_story_card.view.*
import me.mauricee.hackerish.R
import me.mauricee.hackerish.model.Story
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

internal class StoriesAdapter(private val items: List<Story>, private val client: OkHttpClient)
    : RecyclerView.Adapter<StoriesAdapter.ViewHolder>() {


    val selectedItems: Observable<StoriesView.Action>
        get() = itemSubject

    private val itemSubject: PublishSubject<StoriesView.Action> = PublishSubject.create()

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.subscription?.dispose()
        val storyCard = holder.itemView.story_card
        val story = items[position]
        storyCard.clear()
        storyCard.story = story
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            storyCard.transitionName = holder.itemView.context.getString(R.string.transition_story)
        }
        storyCard.clicks.subscribe { itemSubject.onNext(StoriesView.Action(story, it)) }
        holder.subscription = getBodyFromUri(story.url)
                .map(this::getIcons)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    storyCard.faviconUri = it.first
                    storyCard.iconUri = it.second
                }, { Log.e(javaClass.simpleName, "error", it) })
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.subscription?.dispose()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story_card, parent, false)
        return ViewHolder(view)
    }

    private fun getBodyFromUri(url: Uri): Single<String> {
        return if (url == Uri.EMPTY) Single.just("") else
            Single.defer {
                val a = client.newCall(Request.Builder().url(url.toString()).build())
                val body = a.execute().use {
                    it.body()?.string() ?: ""
                }
                Single.just(body)
            }.subscribeOn(Schedulers.io()).onErrorReturnItem("")
    }

    private fun getIcons(html: String): Pair<Uri, Uri> {
        val doc = Jsoup.parse(html)
        val icon = doc.getElementsByTag("meta")
                .filter { (it.attr("property").toString()).equals("og:image", true) }
                .map { Uri.parse(it.attr("content")) }
                .firstOrNull() ?: Uri.EMPTY
        val e = doc.head().select("link[href~=.*\\.(ico|png)]").firstOrNull()
        val faviconUrl = e?.attr("href") ?: ""
        val favicon = if (faviconUrl.isEmpty()) Uri.EMPTY else Uri.parse(faviconUrl)
        return Pair(favicon, icon)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var subscription: Disposable? = null
    }
}