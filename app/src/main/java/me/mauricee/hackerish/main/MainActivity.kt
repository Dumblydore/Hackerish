package me.mauricee.hackerish.main

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import com.jakewharton.rxbinding2.support.design.widget.RxBottomNavigationView
import com.ncapdevi.fragnav.FragNavController
import me.mauricee.hackerish.HackerishActivity
import me.mauricee.hackerish.R
import me.mauricee.hackerish.rx.put
import me.mauricee.hackerish.main.comments.CommentsFragment
import me.mauricee.hackerish.main.stories.StoriesFragment
import me.mauricee.hackerish.model.Story
import android.content.Intent


class MainActivity : HackerishActivity(), MainActivityNavigator {
    //TODO Make this an internal view
    override fun navigateToUrl(uri: Uri) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = uri
        startActivity(i)
    }

    internal var selectedItem = -1
    private lateinit var controller: FragNavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        controller = FragNavController.Builder(savedInstanceState, supportFragmentManager, R.id.content)
                .rootFragments(listOf(StoriesFragment.newInstance(StoriesFragment.TopStories),
                        StoriesFragment.newInstance(StoriesFragment.NewStories),
                        StoriesFragment.newInstance(StoriesFragment.TopStories)))
                .build()

        RxBottomNavigationView.itemSelections(findViewById(R.id.navigation))
                .map(this::mapToIndex)
                .subscribe(controller::switchTab)
                .put(subscriptions)
    }

    private fun mapToIndex(item: MenuItem): Int {
        return when (item.itemId) {
            R.id.nav_top -> 0
            R.id.nav_new -> 1
            R.id.nav_fav -> 2
            else -> throw RuntimeException("Invalid Menu Item!")
        }
    }

    override fun displayStoryDetails(item: Story) {
        controller.pushFragment(CommentsFragment.newInstance(item))
    }

    override fun pop() {
        controller.popFragment()
    }

    override fun onBackPressed() {
        if (controller.isRootFragment)
            super.onBackPressed()
        else
            controller.popFragment()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        controller.onSaveInstanceState(outState)
    }
}
