package me.mauricee.hackerish.main.stories

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import io.reactivex.schedulers.Schedulers
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import kotlinx.android.synthetic.main.fragment_main.*
import me.mauricee.hackerish.HackerishFragment
import me.mauricee.hackerish.R
import me.mauricee.hackerish.model.Story
import me.mauricee.hackerish.rx.put
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
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

    @Inject
    lateinit var client: OkHttpClient

    private val refresh: SwipeRefreshLayout
        get() = view!!.findViewById(R.id.container)

    private val storyList: RecyclerView
        get() = view!!.findViewById(R.id.story_list)

    private val stories = mutableListOf<Story>()
    private lateinit var adapter: StoriesAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(StoriesViewModel::class.java)
        adapter = StoriesAdapter(stories, client)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_stories, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.stories_action_refresh -> {
                viewModel.refresh()
                true
            }
            R.id.stories_action_filter_top -> {
                viewModel.setFilter(StoryFilter.Top)
                true
            }
            R.id.stories_action_filter_new -> {
                viewModel.setFilter(StoryFilter.New)
                true
            }
            R.id.stories_action_filter_best -> {
                viewModel.setFilter(StoryFilter.Best)
                true
            }
            R.id.stories_action_filter_ask -> {
                viewModel.setFilter(StoryFilter.Ask)
                true
            }
            R.id.stories_action_filter_show -> {
                viewModel.setFilter(StoryFilter.Show)
                true
            }
            R.id.stories_action_filter_job -> {
                viewModel.setFilter(StoryFilter.Job)
                true
            }
            else -> false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val a = activity as AppCompatActivity
        a.setSupportActionBar(toolbar)
        storyList.itemAnimator = SlideInRightAnimator()
        storyList.layoutManager = LinearLayoutManager(context)
        storyList.adapter = adapter

        adapter.selectedItems.subscribe(viewModel::select).put(subscriptions)
        viewModel.state.doOnNext {
            toolbar.setTitle(it.filter.title)
            val count = stories.size
            stories.clear()
            adapter.notifyItemRangeRemoved(0, count)
            refresh.isRefreshing = true
        }.flatMap { it.stories.doOnComplete { refresh.isRefreshing = false } }
                .subscribe {
                    stories.add(it)
                    adapter.notifyItemInserted(stories.size)
                }.put(subscriptions)

        RxSwipeRefreshLayout.refreshes(refresh).subscribe { viewModel.refresh() }.put(subscriptions)

    }
}
