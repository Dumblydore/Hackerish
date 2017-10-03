package me.mauricee.hackerish.main.comments

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.mauricee.hackerish.HackerishFragment
import me.mauricee.hackerish.R
import me.mauricee.hackerish.domain.hackerNews.Item

internal class CommentsFragment : HackerishFragment<CommentsViewModel>() {

    companion object {
        val KEY = "${CommentsFragment::class.java.simpleName}.selectedItem"
    }

    private val storyList: RecyclerView
        get() = view!!.findViewById(R.id.story_list)

    private var selectedItem: Int = -1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CommentsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onStart() {
        super.onStart()
        arguments.getInt(KEY, selectedItem)
        Log.d(javaClass.simpleName, "stared")
        val stories = mutableListOf<Item>()
        storyList.layoutManager = LinearLayoutManager(context)
        val adapter = CommentsAdapter(stories)
        storyList.adapter = adapter
        viewModel.comments(selectedItem)
                .subscribe({ stories.add(it); adapter.notifyItemInserted(stories.size) })
    }
}