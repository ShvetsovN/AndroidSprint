package com.animus.androidsprint.ui.categories

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.animus.androidsprint.Constants
import com.animus.androidsprint.model.Category
import com.animus.androidsprint.R
import com.animus.androidsprint.databinding.ItemCategoryBinding
import com.bumptech.glide.Glide

class CategoriesListAdapter(var dataSet: List<Category> = listOf()) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(category: Category)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(item: View) :
        RecyclerView.ViewHolder(item) {
        private val binding = ItemCategoryBinding.bind(item)
        fun bind(itemView: Category) = with(binding) {
            tvItemHeader.text = itemView.title
            tvItemDescription.text = itemView.description
            loadImage(itemView.imageUrl)
        }

        private fun loadImage(imageUrl: String) {
            val fullImageUrl = Constants.IMAGE_URL + imageUrl
            Log.e("!!!", "CategoryListAdapter Loading image from URL: $fullImageUrl")
            Glide.with(itemView.context)
                .load(fullImageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.ivItemImage)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_category, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val category = dataSet[position]
        viewHolder.bind(category)
        viewHolder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(category)
        }
    }

    override fun getItemCount() = dataSet.size
}



