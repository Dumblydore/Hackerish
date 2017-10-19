package me.mauricee.hackerish.main.comments

import me.mauricee.hackerish.HackerishViewModel
import me.mauricee.hackerish.model.HackerNewsManager
import javax.inject.Inject

class CommentsViewModel @Inject constructor(private val manager: HackerNewsManager) : HackerishViewModel() {
    internal fun comments(selectedItem: Int) = manager.commentsFor(selectedItem)
}