package me.mauricee.hackerish.main.stories

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Flowable
import me.mauricee.hackerish.HackerishFragment
import me.mauricee.hackerish.R
import me.mauricee.hackerish.model.Story
import me.mauricee.hackerish.rx.put

internal class StoriesFragment : HackerishFragment<StoriesViewModel>() {

    companion object {
        val NewStories = "NEW"
        val TopStories = "TOP"

        fun newInstance(type: String): StoriesFragment {
            val fragment = StoriesFragment()
            val b = Bundle()
            b.putString("KEY", type)
            fragment.arguments = b
            return fragment
        }
    }

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

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val stories = mutableListOf<Story>()
        storyList.layoutManager = LinearLayoutManager(context)
        val adapter = StoriesAdapter(stories, picasso)
        storyList.adapter = adapter
        getNewsStream().subscribe({ stories.add(it); adapter.notifyItemInserted(stories.size) })
                .put(subscriptions)
        adapter.selectedItems.subscribe(viewModel::select)
                .put(subscriptions)
    }

    private fun getNewsStream(): Flowable<Story> {
        return when (arguments.getString("KEY")) {
            NewStories -> viewModel.newStories
            else -> viewModel.topStories
        }
    }
}
