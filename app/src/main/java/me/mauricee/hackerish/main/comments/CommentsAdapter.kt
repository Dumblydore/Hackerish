package me.mauricee.hackerish.main.comments

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import me.mauricee.hackerish.R
import me.mauricee.hackerish.model.Comment

internal class CommentsAdapter(private val items: List<Comment>,
                               private val isRootComment: Boolean = true,
                               private val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()) :
        RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    private val itemSubject: PublishSubject<Comment> = PublishSubject.create()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int =
            if (isRootComment) R.layout.item_comment_root else R.layout.item_comment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.comment.text = item.text
        holder.user.text = item.author
        holder.date.text = "Posted on ${item.date}"

        RxView.clicks(holder.itemView).subscribe { itemSubject.onNext(item) }
        holder.replies.recycledViewPool = viewPool

        if (item.hasReplies) {
            holder.replies.addItemDecoration(DividerItemDecoration(holder.itemView.context, DividerItemDecoration.VERTICAL))
            holder.replies.visibility = View.VISIBLE
            holder.replies.isNestedScrollingEnabled = false
            val replies = mutableListOf<Comment>()
            holder.replies.layoutManager = LinearLayoutManager(holder.itemView.context)

            val repliesAdapter = CommentsAdapter(replies, false, viewPool)
            holder.replies.adapter = repliesAdapter
            item.replies.subscribe {
                replies.add(it)
                repliesAdapter.notifyItemInserted(replies.size)
            }
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val user: TextView
            get() = itemView.findViewById(R.id.user)
        val date: TextView
            get() = itemView.findViewById(R.id.date)
        val comment: TextView
            get() = itemView.findViewById(R.id.comment)
        val replies: RecyclerView
            get() = itemView.findViewById(R.id.replies)
    }
}