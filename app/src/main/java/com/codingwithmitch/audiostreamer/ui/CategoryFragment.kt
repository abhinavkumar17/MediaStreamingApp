package com.codingwithmitch.audiostreamer.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
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
import com.codingwithmitch.audiostreamer.adapters.CategoryRecyclerAdapter
import com.codingwithmitch.audiostreamer.adapters.CategoryRecyclerAdapter.ICategorySelector
import com.codingwithmitch.audiostreamer.models.Artist
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class CategoryFragment : Fragment(), ICategorySelector {
    // UI Components
    private lateinit var mRecyclerView: RecyclerView

    // Vars
    private var mAdapter: CategoryRecyclerAdapter? = null
    private val mArtists = ArrayList<Artist>()
    private var mIMainActivity: IMainActivity? = null
    private var mSelectedCategory: String? = null
    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            mIMainActivity!!.setActionBarTitle(mSelectedCategory)
        }
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            mSelectedCategory = getArguments()?.getString("category")
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
        mIMainActivity!!.setActionBarTitle(mSelectedCategory)
    }

    private fun retrieveArtists() {
        mIMainActivity!!.showPrgressBar()
        val firestore = FirebaseFirestore.getInstance()
        val query: Query = firestore
            .collection(getString(R.string.collection_audio))
            .document(getString(R.string.document_categories))
            .collection(mSelectedCategory!!)
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    mArtists.add(document.toObject(Artist::class.java))
                }
            } else {
                Log.d(
                    TAG,
                    "onComplete: error getting documents: " + task.exception
                )
            }
            updateDataSet()
        }
    }

    private fun updateDataSet() {
        mIMainActivity!!.hideProgressBar()
        mAdapter!!.notifyDataSetChanged()
    }

    private fun initRecyclerView(view: View) {
        mRecyclerView = view.findViewById(R.id.recycler_view)
        mRecyclerView.setLayoutManager(LinearLayoutManager(getActivity()))
        mAdapter = getActivity()?.let { CategoryRecyclerAdapter(it, mArtists, this) }
        mRecyclerView.setAdapter(mAdapter)
        if (mArtists.size == 0) {
            retrieveArtists()
        }
    }

    override fun onArtistSelected(position: Int) {
        mIMainActivity!!.onArtistSelected(mSelectedCategory, mArtists[position])
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mIMainActivity = getActivity() as IMainActivity?
    }

    companion object {
        private const val TAG = "CategoryFragment"
        fun newInstance(category: String?): CategoryFragment {
            val categoryFragment = CategoryFragment()
            val args = Bundle()
            args.putString("category", category)
            categoryFragment.setArguments(args)
            return categoryFragment
        }
    }
}