package me.mauricee.hackerish.main.stories

import android.support.annotation.StringRes
import me.mauricee.hackerish.R
import me.mauricee.hackerish.model.HackerNewsManager

enum class StoryFilter(@StringRes val title: Int, val type: HackerNewsManager.StoryType) {
    Top(R.string.stories_action_filter_top, HackerNewsManager.StoryType.Top),
    New(R.string.stories_action_filter_new, HackerNewsManager.StoryType.New),
    Best(R.string.stories_action_filter_best, HackerNewsManager.StoryType.Best),
    Ask(R.string.stories_action_filter_ask, HackerNewsManager.StoryType.Ask),
    Show(R.string.stories_action_filter_show, HackerNewsManager.StoryType.Show),
    Job(R.string.stories_action_filter_job, HackerNewsManager.StoryType.Job)
}