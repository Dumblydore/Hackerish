package me.mauricee.hackerish.ui.stories

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.mauricee.hackerish.R
import me.mauricee.hackerish.model.HackerNewsApi
import me.mauricee.hackerish.model.Item
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment() {

    lateinit var api: HackerNewsApi
    val storyList: RecyclerView
        get() = view!!.findViewById(R.id.story_list)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder().build())
                .baseUrl("https://hacker-news.firebaseio.com")
                .build()
                .create(HackerNewsApi::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onStart() {
        super.onStart()
        val stories = mutableListOf<Item>()
        storyList.layoutManager = LinearLayoutManager(context)
        val adapter = ItemAdapter(stories)
        storyList.adapter = adapter
        api.newStories.flatMapIterable { it }
                .flatMapSingle { api.getItem(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    stories.add(it)
                    adapter.notifyItemInserted(stories.size)
                }, { Log.e(javaClass.simpleName, "Failed!", it) })
        adapter.selectedItems.subscribe{Log.i(javaClass.simpleName, "item: $it")}
    }
}
