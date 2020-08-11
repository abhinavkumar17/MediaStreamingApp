package com.codingwithmitch.audiostreamer.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Artist(var title:String, var image:String, var artist_id:String) : Parcelable