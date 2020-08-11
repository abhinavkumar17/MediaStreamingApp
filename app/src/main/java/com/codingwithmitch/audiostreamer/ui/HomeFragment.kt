package com.codingwithmitch.audiostreamer.ui

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
import com.codingwithmitch.audiostreamer.adapters.HomeRecyclerAdapter
import com.codingwithmitch.audiostreamer.adapters.HomeRecyclerAdapter.IHomeSelector
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class HomeFragment : Fragment(), IHomeSelector {
    // UI Components
    private lateinit var mRecyclerView: RecyclerView

    // Vars
    private lateinit var mAdapter: HomeRecyclerAdapter
    private val mCategories =  ArrayList<String>()
    private lateinit var mIMainActivity: IMainActivity

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            mIMainActivity.setActionBarTitle(getString(R.string.categories))
        }
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        mIMainActivity.setActionBarTitle(getString(R.string.categories))
    }

    private fun retrieveCategories() {
        mIMainActivity.showPrgressBar()
        val firestore = FirebaseFirestore.getInstance()
        val ref = firestore
            .collection(getString(R.string.collection_audio))
            .document(getString(R.string.document_categories))
        ref.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val doc = task.result
                Log.d(TAG, "onComplete: $doc")
                val categoriesMap: HashMap<String, String> = (doc!!.data!!["categories"] as HashMap<*, *>?)!! as HashMap<String, String>
                mCategories.addAll(categoriesMap.keys)
            }
            updateDataSet()
        }
    }

     fun updateDataSet() {
        mIMainActivity.hideProgressBar()
        mAdapter.notifyDataSetChanged()
    }

    private fun initRecyclerView(view: View) {
        mRecyclerView = view.findViewById(R.id.recycler_view)
        mRecyclerView.setLayoutManager(LinearLayoutManager(getActivity()))
        mAdapter = getActivity()?.let { HomeRecyclerAdapter(mCategories, it, this) }!!
        mRecyclerView.setAdapter(mAdapter)
        if (mCategories.size == 0) {
            retrieveCategories()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mIMainActivity = (getActivity() as IMainActivity?)!!
    }

    override fun onCategorySelected(postion: Int) {
        Log.d(TAG, "onCategorySelected: list item is clicked!")
        mIMainActivity.onCategorySelected(mCategories[postion])
    }

    companion object {
        private const val TAG = "HomeFragment"
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}
