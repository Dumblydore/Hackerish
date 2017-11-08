package me.mauricee.hackerish.main.comments

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_comments.*
import me.mauricee.hackerish.HackerishFragment
import me.mauricee.hackerish.R
import me.mauricee.hackerish.model.Comment
import me.mauricee.hackerish.model.Story

internal class CommentsFragment : HackerishFragment<CommentsViewModel>() {

    companion object {
        val KEY = "${CommentsFragment::class.java.simpleName}.selectedItem"

        fun newInstance(story: Story): CommentsFragment {
            val bundle = Bundle()
            bundle.putParcelable(KEY, story)
            val fragment = CommentsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CommentsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_comments, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buildCommentList()
    }

    private fun buildCommentList() {
        val story: Story = arguments.getParcelable(KEY)
        val comments = mutableListOf<Comment>()
        story_list.isNestedScrollingEnabled = false
        story_list.layoutManager = LinearLayoutManager(context)
        val colors = activity.resources.getIntArray(R.array.commentColors).toList()
        val adapter = CommentsAdapter(comments, colors)
        story_list.adapter = adapter
        viewModel.comments(story.id)
                .subscribe({ comments.add(it); adapter.notifyItemInserted(comments.size) },
                        {
                            Toast.makeText(activity, "Error loading comments.", Toast.LENGTH_LONG)
                                    .show()
                        })
    }
}