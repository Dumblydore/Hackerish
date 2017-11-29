package me.mauricee.hackerish.main.stories

import android.net.Uri
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import me.mauricee.hackerish.HackerishViewModel
import me.mauricee.hackerish.main.MainActivityNavigator
import me.mauricee.hackerish.model.HackerNewsManager
import okhttp3.Cache
import javax.inject.Inject

internal class StoriesViewModel @Inject constructor(private val navigator: MainActivityNavigator,
                                                    private val hackerNewsManager: HackerNewsManager,
                                                    private val cache: Cache) : HackerishViewModel() {

    private val stateSubject: BehaviorSubject<StoriesView.State> = BehaviorSubject.create()
    val state: Observable<StoriesView.State> = stateSubject

    init {
        setFilter(StoryFilter.Top)
    }

    fun setFilter(filter: StoryFilter) {
        val stories = hackerNewsManager.stories(filter.type).toObservable().cache()
        stateSubject.onNext(StoriesView.State(filter, stories))
    }

    fun select(item: StoriesView.Action) {
        if (item.navigateToPage && item.story.url != Uri.EMPTY)
            navigator.navigateToUrl(item.story.url)
        else
            navigator.displayStoryDetails(item.story)
    }

    fun refresh() {
        cache.evictAll()
        setFilter(stateSubject.value.filter)
    }

}