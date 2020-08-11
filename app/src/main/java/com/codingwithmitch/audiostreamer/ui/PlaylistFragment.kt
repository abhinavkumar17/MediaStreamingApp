package com.codingwithmitch.audiostreamer.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codingwithmitch.audiostreamer.R
import com.codingwithmitch.audiostreamer.adapters.PlaylistRecyclerAdapter
import com.codingwithmitch.audiostreamer.adapters.PlaylistRecyclerAdapter.IMediaSelector
import com.codingwithmitch.audiostreamer.models.Artist
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.*

class PlaylistFragment : Fragment(), IMediaSelector {
    // UI Components
    private lateinit var mRecyclerView: RecyclerView

    // Vars
    private var mAdapter: PlaylistRecyclerAdapter? = null
    private val mMediaList =
        ArrayList<MediaMetadataCompat>()
    private var mIMainActivity: IMainActivity? = null
    private var mSelectedCategory: String? = null
    private var mSelectArtist: Artist? = null
    private var mSelectedMedia: MediaMetadataCompat? = null

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            mIMainActivity!!.setActionBarTitle(mSelectArtist!!.title)
        }
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            mSelectedCategory = getArguments()?.getString("category")
            mSelectArtist = getArguments()?.getParcelable("artist")
        }
        setRetainInstance(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        initRecyclerView(view)
        mIMainActivity!!.setActionBarTitle(mSelectArtist!!.title)
    }

    private fun retrieveMedia() {
        mIMainActivity!!.showPrgressBar()
        val firestore = FirebaseFirestore.getInstance()
        val query: Query = firestore
            .collection(getString(R.string.collection_audio))
            .document(getString(R.string.document_categories))
            .collection(mSelectedCategory!!)
            .document(mSelectArtist!!.artist_id)
            .collection(getString(R.string.collection_content))
            .orderBy(getString(R.string.field_date_added), Query.Direction.ASCENDING)
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    addToMediaList(document)
                }
            } else {
                Log.d(
                    PlaylistFragment.Companion.TAG,
                    "onComplete: error getting documents: " + task.exception
                )
            }
            updateDataSet()
        }
    }

    private fun addToMediaList(document: QueryDocumentSnapshot) {
        val media = MediaMetadataCompat.Builder()
            .putString(
                MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
                document.getString(getString(R.string.field_media_id))
            )
            .putString(
                MediaMetadataCompat.METADATA_KEY_ARTIST,
                document.getString(getString(R.string.field_artist))
            )
            .putString(
                MediaMetadataCompat.METADATA_KEY_TITLE,
                document.getString(getString(R.string.field_title))
            )
            .putString(
                MediaMetadataCompat.METADATA_KEY_MEDIA_URI,
                document.getString(getString(R.string.field_media_url))
            )
            .putString(
                MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION,
                document.getString(getString(R.string.field_description))
            )
            .putString(
                MediaMetadataCompat.METADATA_KEY_DATE,
                document.getDate(getString(R.string.field_date_added)).toString()
            )
            .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, mSelectArtist!!.image)
            .build()
        mMediaList.add(media)
    }

    private fun updateDataSet() {
        mIMainActivity!!.hideProgressBar()
        mAdapter!!.notifyDataSetChanged()
    }

    private fun initRecyclerView(view: View) {
        mRecyclerView = view.findViewById(R.id.recycler_view)
        mRecyclerView.setLayoutManager(LinearLayoutManager(getActivity()))
        mAdapter = getActivity()?.let { PlaylistRecyclerAdapter(it, mMediaList, this) }
        mRecyclerView.setAdapter(mAdapter)
        if (mMediaList.size == 0) {
            retrieveMedia()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mIMainActivity = getActivity() as IMainActivity?
    }

    override fun onMediaSelected(position: Int) {
       // mIMainActivity.getMyApplicationInstance().setMediaItems(mMediaList)
        mSelectedMedia = mMediaList[position]
        mAdapter!!.setSelectedIndex(position)
       /* mIMainActivity.onMediaSelected(
            mSelectArtist!!.artist_id,  // playlist_id = artist_id
            mMediaList[position],
            position
        )*/
        saveLastPlayedSongProperties()
    }

    private fun saveLastPlayedSongProperties() {
        // Save some properties for next time the app opens
        // NOTE: Normally you'd do this with a cache
       /* mIMainActivity.getMyPreferenceManager()
            .savePlaylistId(mSelectArtist!!.artist_id) // playlist id is same as artist id*/
    }

    companion object {
        private const val TAG = "PlaylistFragment"
        fun newInstance(category: String?, artist: Artist?): PlaylistFragment {
            val playlistFragment = PlaylistFragment()
            val args = Bundle()
            args.putString("category", category)
            args.putParcelable("artist", artist)
            playlistFragment.setArguments(args)
            return playlistFragment
        }
    }
}