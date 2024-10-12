package com.animus.androidsprint.ui.recipes.recipeList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.animus.androidsprint.Constants
import com.animus.androidsprint.R
import com.animus.androidsprint.model.Recipe
import com.animus.androidsprint.databinding.ItemRecipeBinding
import com.bumptech.glide.Glide

class RecipeListAdapter(var dataSet: List<Recipe> = listOf()) :
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
            Log.e("RecipeListAdapter", "bind - Recipe: ${itemView.title}, ID: ${itemView.id}")
            tvItemRecipeHeader.text = itemView.title
            loadImage(itemView.imageUrl)
        }

        private fun loadImage(imageUrl: String) {
            val fullImageUrl = Constants.IMAGE_URL + imageUrl
            Glide.with(itemView.context)
                .load(fullImageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.ivItemRecipeImage)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_recipe, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val recipe = dataSet[position]
        Log.e(
            "RecipeListAdapter",
            "on BVH - Position: $position, Recipe: ${recipe.title}, ID: ${recipe.id}"
        )
        viewHolder.bind(recipe)
        viewHolder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(recipe.id)
        }
    }

    override fun getItemCount() = dataSet.size

}



