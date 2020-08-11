package com.codingwithmitch.audiostreamer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.codingwithmitch.audiostreamer.R
import com.codingwithmitch.audiostreamer.models.Artist

class CategoryRecyclerAdapter(
    context: Context,
    artists: ArrayList<Artist>,
    categorySelector: ICategorySelector
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private var mArtists: ArrayList<Artist> = ArrayList<Artist>()
    private val mContext: Context
    private val mICategorySelector: ICategorySelector



    override fun onBindViewHolder(@NonNull viewHolder: RecyclerView.ViewHolder, i: Int) {
        (viewHolder as ViewHolder).title.setText(mArtists[i].title)
        val options: RequestOptions = RequestOptions()
            .error(R.drawable.ic_launcher_background)
        Glide.with(mContext)
            .setDefaultRequestOptions(options)
            .load(mArtists[i].image)
            .into((viewHolder as ViewHolder).image)
    }

    inner class ViewHolder(
        @NonNull itemView: View,
        categorySelector: ICategorySelector
    ) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val title: TextView
        val image: ImageView
        private val iCategorySelector: ICategorySelector
        override fun onClick(view: View) {
            iCategorySelector.onArtistSelected(getAdapterPosition())
        }

        init {
            title = itemView.findViewById(R.id.title)
            image = itemView.findViewById(R.id.image)
            iCategorySelector = categorySelector
            itemView.setOnClickListener(this)
        }
    }

    interface ICategorySelector {
        fun onArtistSelected(position: Int)
    }

    init {
        mArtists = artists
        mContext = context
        mICategorySelector = categorySelector
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(mContext).inflate(R.layout.layout_artist_list_item, null)
        return ViewHolder(view, mICategorySelector)
    }

    override fun getItemCount(): Int {
       return mArtists.size
    }
}