package com.codingwithmitch.audiostreamer.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.codingwithmitch.audiostreamer.R
import com.codingwithmitch.audiostreamer.models.Artist

class MainActivity : AppCompatActivity(),IMainActivity {

    //UI Components
    private var mProgressBar: ProgressBar? = null

    // Vars


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mProgressBar = findViewById(R.id.progress_bar)
        if (savedInstanceState == null) {
            loadFragment(HomeFragment.newInstance(), true)
        }
    }

    private fun loadFragment(
        fragment: Fragment,
        lateralMovement: Boolean
    ) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
       /* if (lateralMovement) {
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
        }*/
        var tag = ""

        if (fragment is HomeFragment) {
            tag = getString(R.string.fragment_home)
        } else if (fragment is CategoryFragment) {
            tag = getString(R.string.fragment_category)
            transaction.addToBackStack(tag)
        } else if (fragment is PlaylistFragment) {
            tag = getString(R.string.fragment_playlist)
            transaction.addToBackStack(tag)
        }
        transaction.replace(R.id.main_container, fragment, tag)
        transaction.commit()
       // MainActivityFragmentManager.getInstance().addFragment(fragment)
        //showFragment(fragment, false)
    }

    override fun hideProgressBar() {
        mProgressBar!!.visibility = View.GONE
    }

    override fun showPrgressBar() {
        mProgressBar!!.visibility = View.VISIBLE
    }

    override fun onCategorySelected(category: String?) {
        loadFragment(CategoryFragment.newInstance(category), true)
    }

    override fun onArtistSelected(category: String?, artist: Artist?) {
        loadFragment(PlaylistFragment.newInstance(category, artist), true)
    }

    override fun setActionBarTitle(title: String?) {
        supportActionBar!!.title = title
    }
}