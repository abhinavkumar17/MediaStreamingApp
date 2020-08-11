package com.codingwithmitch.audiostreamer.ui

import com.codingwithmitch.audiostreamer.models.Artist

interface IMainActivity {

    fun hideProgressBar()

    fun showPrgressBar()

    fun onCategorySelected(category: String?)

    fun onArtistSelected(category: String?, artist: Artist?)

    fun setActionBarTitle(title: String?)
}