package me.mauricee.hackerish.main.stories

import io.reactivex.Flowable
import me.mauricee.hackerish.HackerishViewModel
import me.mauricee.hackerish.main.MainActivityNavigator
import me.mauricee.hackerish.model.HackerNewsManager
import me.mauricee.hackerish.model.Story
import javax.inject.Inject

internal class StoriesViewModel @Inject constructor(private val navigator: MainActivityNavigator,
                                                    hackerNewsManager: HackerNewsManager) : HackerishViewModel() {

    var topStories: Flowable<Story> = hackerNewsManager.topStories().cache()

    val newStories = hackerNewsManager.newStories().cache()!!

    fun select(item: Story) = navigator.displayStoryDetails(item)


}