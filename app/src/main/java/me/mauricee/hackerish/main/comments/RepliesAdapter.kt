package me.mauricee.hackerish.main.comments

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import me.mauricee.hackerish.R
import me.mauricee.hackerish.model.Item

class RepliesAdapter(private val replies: List<Item>, private val viewPool: RecyclerView.RecycledViewPool) : RecyclerView.Adapter<RepliesAdapter.ViewHolder>() {

//    private var isExpanded: fa

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = replies[position]
        holder.text.text = item.text
        holder.replies.visibility = if (item.descendants.isNotEmpty()) View.VISIBLE else View.GONE
        holder.replies.recycledViewPool = viewPool
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_reply, parent, false))

    override fun getItemCount() = replies.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView
            get() = itemView.findViewById(R.id.comment)
        val replies: RecyclerView
            get() = itemView.findViewById(R.id.replies)
    }
}