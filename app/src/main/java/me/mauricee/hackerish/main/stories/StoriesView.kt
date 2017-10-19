package me.mauricee.hackerish.main.stories

import me.mauricee.hackerish.model.Story

class StoriesView {

    data class Action(val story: Story, val navigateToPage: Boolean = false)

}
