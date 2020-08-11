package com.codingwithmitch.audiostreamer.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.codingwithmitch.audiostreamer.R

class MediaControllerFragment : Fragment(), View.OnClickListener {
    // UI Components
    private var mSongTitle: TextView? = null
    private var mPlayPause: ImageView? = null
    //private var mSeekBarAudio: MediaSeekBar? = null

    // Vars
    private var mIMainActivity: IMainActivity? = null

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_media_controller, container, false)
    }

    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        mSongTitle = view.findViewById(R.id.media_song_title)
        mPlayPause = view.findViewById(R.id.play_pause)
       // mSeekBarAudio = view.findViewById(R.id.seekbar_audio)
      //  mPlayPause.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.play_pause) {
          //  mIMainActivity.playPause()
        }
    }

    fun setIsPlaying(isPlaying: Boolean) {
        if (isPlaying) {
            getActivity()?.let {
                mPlayPause?.let { it1 ->
                    Glide.with(it)
                        .load(R.drawable.ic_pause_circle_outline_white_24dp)
                        .into(it1)
                }
            }
        } else {
            getActivity()?.let {
                mPlayPause?.let { it1 ->
                    Glide.with(it)
                        .load(R.drawable.ic_play_circle_outline_white_24dp)
                        .into(it1)
                }
            }
        }
    }

    fun setMediaTitle(mediaItem: MediaMetadataCompat) {
        mSongTitle!!.text = mediaItem.description.title
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mIMainActivity = getActivity() as IMainActivity?
    }

    companion object {
        private const val TAG = "MediaControllerFragment"
    }
}