package com.animus.androidsprint

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.animus.androidsprint.databinding.ItemRecipeBinding
import java.io.IOException
import java.io.InputStream

class RecipeListAdapter(private val dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(item: View) :
        RecyclerView.ViewHolder(item) {
        private val binding = ItemRecipeBinding.bind(item)
        fun bind(itemView: Recipe) = with(binding) {
            tvItemRecipeHeader.text = itemView.title
            loadImageFromAsset(itemView.imageUrl)
        }

        private fun loadImageFromAsset(imageUrl: String) {
            try {
                val inputStream: InputStream = itemView.context.assets.open(imageUrl)
                val drawable = Drawable.createFromStream(inputStream, null)
                binding.ivItemRecipeImage.setImageDrawable(drawable)
            } catch (ex: IOException) {
                Log.e("ViewHolder", Log.getStackTraceString(ex))
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_recipe, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val recipe = dataSet[position]
        viewHolder.bind(recipe)
        viewHolder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(recipe.id)
        }
    }

    override fun getItemCount() = dataSet.size

}



