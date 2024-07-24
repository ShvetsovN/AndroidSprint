package com.animus.androidsprint.ui.categories

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.animus.androidsprint.model.Category
import com.animus.androidsprint.R
import com.animus.androidsprint.databinding.ItemCategoryBinding
import java.io.IOException
import java.io.InputStream

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
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
            loadImageFromAsset(itemView.imageUrl)
        }

        private fun loadImageFromAsset(imageUrl: String) {
            try {
                val inputStream: InputStream = itemView.context.assets.open(imageUrl)
                val drawable = Drawable.createFromStream(inputStream, null)
                binding.ivItemImage.setImageDrawable(drawable)
            } catch (ex: IOException) {
                Log.e("ViewHolder", Log.getStackTraceString(ex))
            }
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
            itemClickListener?.onItemClick(category.id)
        }
    }

    override fun getItemCount() = dataSet.size

}



