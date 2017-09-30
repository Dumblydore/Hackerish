package me.mauricee.hackerish.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import me.mauricee.hackerish.HackerishActivity
import me.mauricee.hackerish.R
import me.mauricee.hackerish.main.comments.CommentsFragment
import me.mauricee.hackerish.main.stories.StoriesFragment
import me.mauricee.hackerish.model.Item

class MainActivity : HackerishActivity(), MainActivityNavigator {

    internal var selectedItem = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        if (supportFragmentManager.backStackEntryCount == 0)
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content, StoriesFragment())
                    .addToBackStack(null)
                    .commit()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun displayItemDetails(item: Item) {
        selectedItem = item.id
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content, CommentsFragment())
                .addToBackStack(null)
                .commit()
    }

    override fun pop() {
        supportFragmentManager.popBackStack()
    }

}
