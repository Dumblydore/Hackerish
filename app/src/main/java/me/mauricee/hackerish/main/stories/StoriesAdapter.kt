package me.mauricee.hackerish.main.stories

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import me.mauricee.hackerish.R
import me.mauricee.hackerish.model.Item

internal class StoriesAdapter(private val items: List<Item>) : RecyclerView.Adapter<StoriesAdapter.ViewHolder>() {

    private val itemSubject: PublishSubject<Item> = PublishSubject.create()
    val selectedItems: Observable<Item>
        get() = itemSubject

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.subtitle.text = "by ${item.by}"
        RxView.clicks(holder.itemView).subscribe { itemSubject.onNext(item) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return ViewHolder(view)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView
            get() = itemView.findViewById(R.id.title)
        val subtitle: TextView
            get() = itemView.findViewById(R.id.subtitle)
    }
}