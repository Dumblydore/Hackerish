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
import me.mauricee.hackerish.model.Comment

internal class CommentsFragment : HackerishFragment<CommentsViewModel>() {

    companion object {
        val KEY = "${CommentsFragment::class.java.simpleName}.selectedItem"

        fun newInstance(storyId: Int): CommentsFragment {
            val bundle = Bundle()
            bundle.putInt(KEY, storyId)
            val fragment = CommentsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val storyList: RecyclerView
        get() = view!!.findViewById(R.id.story_list)


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
        val stories = mutableListOf<Comment>()
        storyList.layoutManager = LinearLayoutManager(context)
        val adapter = CommentsAdapter(stories)
        storyList.adapter = adapter
        viewModel.comments(arguments.getInt(KEY, -1))
                .subscribe({ stories.add(it); adapter.notifyItemInserted(stories.size) }, { Log.e(javaClass.simpleName, "Failed!", it) })
    }
}