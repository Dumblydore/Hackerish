package me.mauricee.hackerish.main.stories

import io.reactivex.Observable
import me.mauricee.hackerish.model.Story

class StoriesView {

    data class Action(val story: Story, val navigateToPage: Boolean = false)

    data class State(val filter: StoryFilter, val stories: Observable<Story>)
}
