package me.mauricee.hackerish.main.stories

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_story_card.view.*
import me.mauricee.hackerish.R
import me.mauricee.hackerish.model.Story

internal class StoriesAdapter(private val items: List<Story>)
    : RecyclerView.Adapter<StoriesAdapter.ViewHolder>() {


    val selectedItems: Observable<StoriesView.Action>
        get() = itemSubject

    private val itemSubject: PublishSubject<StoriesView.Action> = PublishSubject.create()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val storyCard = holder.itemView.story_card
        val story = items[position]
        storyCard.story = story
        storyCard.clicks.subscribe { itemSubject.onNext(StoriesView.Action(story, it)) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story_card, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}