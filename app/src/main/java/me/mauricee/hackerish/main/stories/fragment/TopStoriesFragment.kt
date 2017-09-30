package me.mauricee.hackerish.main.stories.fragment

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.mauricee.hackerish.HackerishFragment
import me.mauricee.hackerish.R
import me.mauricee.hackerish.main.stories.StoriesAdapter
import me.mauricee.hackerish.main.stories.StoriesViewModel
import me.mauricee.hackerish.model.Item

internal class TopStoriesFragment : HackerishFragment<StoriesViewModel>() {
    private val storyList: RecyclerView
        get() = view!!.findViewById(R.id.story_list)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StoriesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onStart() {
        super.onStart()
        val stories = mutableListOf<Item>()
        storyList.layoutManager = LinearLayoutManager(context)
        val adapter = StoriesAdapter(stories, picasso)
        storyList.adapter = adapter
        viewModel.topStories.subscribe({ stories.add(it); adapter.notifyItemInserted(stories.size) })
        adapter.selectedItems.subscribe(viewModel::select)
    }
}