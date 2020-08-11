package com.codingwithmitch.audiostreamer.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.codingwithmitch.audiostreamer.R

class HomeRecyclerAdapter(
    mCategories: ArrayList<String>,
    mContext: Context,
    mIHomeSelector: IHomeSelector
) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    private var mCategories = ArrayList<String>()
    private val mContext: Context
    private val mIHomeSelector: IHomeSelector

    override fun onBindViewHolder(@NonNull viewHolder: RecyclerView.ViewHolder, i: Int) {
        (viewHolder as ViewHolder).category.text = mCategories[i]
        val requestOptions: RequestOptions = RequestOptions().error(R.drawable.ic_launcher_background)
        var iconResource: Drawable? = null
        when (mCategories[i]) {
            "Music" -> {
                iconResource =
                    ContextCompat.getDrawable(mContext, R.drawable.ic_audiotrack_white_24dp)
            }
            "Podcasts" -> {
                iconResource = ContextCompat.getDrawable(mContext, R.drawable.ic_mic_white_24dp)
            }
        }
        Glide.with(mContext)
            .setDefaultRequestOptions(requestOptions)
            .load(iconResource)
            .into((viewHolder as ViewHolder).category_icon)
    }

    inner class ViewHolder(
        @NonNull itemView: View,
        iHomeSelector: IHomeSelector
    ) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val category: TextView
        val category_icon: ImageView
        private val iHomeSelector: IHomeSelector
        override fun onClick(view: View) {
            iHomeSelector.onCategorySelected(getAdapterPosition())
        }

        init {
            category = itemView.findViewById(R.id.category_title)
            category_icon = itemView.findViewById(R.id.category_icon)
            this.iHomeSelector = iHomeSelector
            itemView.setOnClickListener(this)
        }
    }

    interface IHomeSelector {
        fun onCategorySelected(postion: Int)
    }

    init {
        this.mCategories = mCategories
        this.mContext = mContext
        this.mIHomeSelector = mIHomeSelector
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(mContext).inflate(R.layout.layout_category_list_item, null)
        return ViewHolder(view, mIHomeSelector)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}