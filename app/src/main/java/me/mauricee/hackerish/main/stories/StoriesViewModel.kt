package me.mauricee.hackerish.main.stories

import android.net.Uri
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import me.mauricee.hackerish.HackerishViewModel
import me.mauricee.hackerish.main.MainActivityNavigator
import me.mauricee.hackerish.model.HackerNewsManager
import me.mauricee.hackerish.model.Story
import javax.inject.Inject

internal class StoriesViewModel @Inject constructor(private val navigator: MainActivityNavigator,
                                                    private val hackerNewsManager: HackerNewsManager) : HackerishViewModel() {

    private val stateSubject: BehaviorSubject<StoriesView.State> = BehaviorSubject.create()
    val state: Observable<StoriesView.State> = stateSubject

    fun setFilter(filter: StoryFilter) {
        val stories = hackerNewsManager.topStories().toObservable().cache()
        stateSubject.onNext(StoriesView.State(filter, stories))
    }

    init {
        setFilter(StoryFilter.Story)
    }

    fun select(item: StoriesView.Action) {
        if (item.navigateToPage && item.story.url != Uri.EMPTY)
            navigator.navigateToUrl(item.story.url)
        else
            navigator.displayStoryDetails(item.story)
    }

    fun refresh() {
        setFilter(stateSubject.value.filter)
    }

}