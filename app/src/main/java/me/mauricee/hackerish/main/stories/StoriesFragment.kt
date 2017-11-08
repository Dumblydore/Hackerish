package me.mauricee.hackerish.main.stories

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Flowable
import me.mauricee.hackerish.HackerishFragment
import me.mauricee.hackerish.R
import me.mauricee.hackerish.common.ImageDownloader
import me.mauricee.hackerish.model.Story
import me.mauricee.hackerish.rx.put
import javax.inject.Inject

internal class StoriesFragment : HackerishFragment<StoriesViewModel>() {

    companion object {
        private val StoriesTypeKey = "${StoriesFragment::class.java.canonicalName}.StoriesTypeKey"
        val NewStories = "${StoriesFragment::class.java.canonicalName}.New"
        val TopStories = "${StoriesFragment::class.java.canonicalName}.Top"

        fun newInstance(type: String): StoriesFragment {
            val fragment = StoriesFragment()
            val b = Bundle()
            b.putString(StoriesTypeKey, type)
            fragment.arguments = b
            return fragment
        }
    }

    private val refresh: SwipeRefreshLayout
        get() = view!!.findViewById(R.id.container)

    private val storyList: RecyclerView
        get() = view!!.findViewById(R.id.story_list)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(StoriesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val stories = mutableListOf<Story>()
        storyList.layoutManager = LinearLayoutManager(context)
        val adapter = StoriesAdapter(stories)
        storyList.adapter = adapter

        refresh.isRefreshing = true
        getNewsStream()
                .doOnComplete { refresh.isRefreshing = false }
                .doOnNext { Log.d(javaClass.simpleName, "${it.id} - ${it.title}") }
                .subscribe({ stories.add(it); adapter.notifyItemInserted(stories.size) })
                .put(subscriptions)
        adapter.selectedItems.subscribe(viewModel::select)
                .put(subscriptions)
    }

    private fun getNewsStream(): Flowable<Story> = when (arguments.getString(StoriesTypeKey)) {
        NewStories -> viewModel.newStories
        else -> viewModel.topStories
    }
}
