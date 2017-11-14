package me.mauricee.hackerish.main.stories

import android.support.annotation.StringRes
import me.mauricee.hackerish.R

enum class StoryFilter(@StringRes val title: Int) {
    Ask(R.string.stories_filter_ask),
    Show(R.string.stories_filter_show),
    Job(R.string.stories_filter_job),
    Story(R.string.stories_filter_story)
}