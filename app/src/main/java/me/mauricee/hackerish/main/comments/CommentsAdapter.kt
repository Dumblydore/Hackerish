package me.mauricee.hackerish.main.comments

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import me.mauricee.hackerish.R
import me.mauricee.hackerish.common.logTag
import me.mauricee.hackerish.model.Comment

internal class CommentsAdapter(private val items: List<Comment>,
                               private val colors: List<Int>,
                               private val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()) :
        RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {


    private val itemSubject: PublishSubject<Comment> = PublishSubject.create()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int =
            if (items[position].depth == 0) R.layout.item_comment_root else R.layout.item_comment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.comment.text = item.text
        holder.user.text = "${item.author} Posted on ${item.date}"

        RxView.clicks(holder.itemView).subscribe { itemSubject.onNext(item) }
        holder.replies.recycledViewPool = viewPool

        holder.color?.setBackgroundColor(colors[Math.max(0, (item.depth % colors.size) - 1)])

        holder.replies.isNestedScrollingEnabled = false
        if (item.hasReplies) {
            holder.replies.visibility = View.VISIBLE
            val replies = mutableListOf<Comment>()
            holder.replies.layoutManager = LinearLayoutManager(holder.itemView.context)

            val repliesAdapter = CommentsAdapter(replies, colors, viewPool)
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
        val color: View?
            get() = itemView.findViewById(R.id.color)
        val comment: TextView
            get() = itemView.findViewById(R.id.comment)
        val replies: RecyclerView
            get() = itemView.findViewById(R.id.replies)
    }
}