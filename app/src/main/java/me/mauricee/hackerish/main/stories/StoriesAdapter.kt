package me.mauricee.hackerish.main.stories

import android.content.Context
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.amulyakhare.textdrawable.TextDrawable
import com.jakewharton.rxbinding2.view.RxView
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import me.mauricee.hackerish.R
import me.mauricee.hackerish.model.Story
import me.mauricee.hackerish.widget.CircleTransform

internal class StoriesAdapter(private val items: List<Story>, private val context: Context) : RecyclerView.Adapter<StoriesAdapter.ViewHolder>() {

    companion object {
        private val hnPattern = Regex("(^(\\w+ )(HN:))")
        private val builder = TextDrawable.builder()
        private val circleTransform = CircleTransform()
    }

    private val itemSubject: PublishSubject<StoriesView.Action> = PublishSubject.create()

    val selectedItems: Observable<StoriesView.Action>
        get() = itemSubject

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title.replace(hnPattern, "")


        holder.subtitle.text = "by ${item.user}"

        if (item.title.contains(hnPattern)) {
            holder.host.text = hnPattern.find(item.title)?.value?.replace(":", "") ?: ""
        } else {
            holder.host.text = item.url.host?.replace("www.", "") ?: ""
        }


        Picasso.with(context).load(item.favicon)
                .error(builder.buildRound(item.host.substring(0, 1).toUpperCase(),
                        ContextCompat.getColor(context, R.color.colorPrimary)))
                .fit()
                .transform(circleTransform)
                .into(holder.favicon)

        if (item.url == Uri.EMPTY) {
            holder.icon.visibility = View.GONE
        } else {
            holder.icon.visibility = View.VISIBLE
            Picasso.with(context).load(item.icon)
                    .error(R.drawable.ic_launcher_background)
                    .fit()
                    .centerCrop()
                    .into(holder.icon)
        }
        Observable.merge(RxView.clicks(holder.itemView).map { false },
                RxView.clicks(holder.icon).map { true })
                .subscribe { itemSubject.onNext(StoriesView.Action(item, it)) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story_large, parent, false)
        return ViewHolder(view)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView
            get() = itemView.findViewById(R.id.title)
        val subtitle: TextView
            get() = itemView.findViewById(R.id.subtitle)
        val icon: ImageView
            get() = itemView.findViewById(R.id.icon)
        val host: TextView
            get() = itemView.findViewById(R.id.host)
        val favicon: ImageView
            get() = itemView.findViewById(R.id.favicon)
    }
}