package me.mauricee.hackerish.main.stories

import me.mauricee.hackerish.model.Story

class StoriesState {

    data class Action(val story: Story, val navigateToPage: Boolean = false)

}
