package com.codingwithmitch.audiostreamer.adapters

import android.content.Context
import android.support.v4.media.MediaMetadataCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.codingwithmitch.audiostreamer.R

class PlaylistRecyclerAdapter(
    context: Context,
    mediaList: ArrayList<MediaMetadataCompat>,
    mediaSelector: IMediaSelector
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private var mMediaList: ArrayList<MediaMetadataCompat> =
        ArrayList<MediaMetadataCompat>()
    private val mContext: Context
    private val mIMediaSelector: IMediaSelector
    private var mSelectedIndex: Int

    override fun onBindViewHolder(@NonNull viewHolder: RecyclerView.ViewHolder, i: Int) {
        (viewHolder as ViewHolder).title.setText(
            mMediaList[i].getDescription().getTitle()
        )
        (viewHolder ).artist.setText(
            mMediaList[i].getDescription().getSubtitle()
        )
        if (i == mSelectedIndex) {
            (viewHolder as ViewHolder).title.setTextColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.green
                )
            )
        } else {
            (viewHolder as ViewHolder).title.setTextColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
        }
    }

    fun setSelectedIndex(index: Int) {
        mSelectedIndex = index
        notifyDataSetChanged()
    }

    fun getIndexOfItem(mediaItem: MediaMetadataCompat): Int {
        for (i in mMediaList.indices) {
            if (mMediaList[i].getDescription().getMediaId()
                    .equals(mediaItem.getDescription().getMediaId())
            ) {
                return i
            }
        }
        return -1
    }

    inner class ViewHolder(
        @NonNull itemView: View,
        categorySelector: IMediaSelector
    ) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val title: TextView
        val artist: TextView
        private val iMediaSelector: IMediaSelector
        override fun onClick(view: View) {
            iMediaSelector.onMediaSelected(getAdapterPosition())
        }

        init {
            title = itemView.findViewById(R.id.media_title)
            artist = itemView.findViewById(R.id.media_artist)
            iMediaSelector = categorySelector
            itemView.setOnClickListener(this)
        }
    }

    interface IMediaSelector {
        fun onMediaSelected(position: Int)
    }

    companion object {
        private const val TAG = "PlaylistRecyclerAdapter"
    }

    init {
        println(
            "PlaylistRecyclerAdapter: called."
        )
        mMediaList = mediaList
        mContext = context
        mIMediaSelector = mediaSelector
        mSelectedIndex = -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(mContext).inflate(R.layout.layout_playlist_list_item, null)
        return ViewHolder(view, mIMediaSelector)
    }

    override fun getItemCount(): Int {
        return mMediaList.size
    }
}