package com.animus.androidsprint

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.animus.androidsprint.databinding.ItemIngredientBinding

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ItemIngredientBinding.bind(item)
        fun bind(itemView: Ingredient) = with(binding) {
            tvIngredientDescription.text = itemView.description
            tvIngredientQuantity.text = "${itemView.quantity} ${itemView.unitOfMeasure}"
        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_ingredient, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val ingredient = dataSet[position]
        viewHolder.bind(ingredient)
    }

    override fun getItemCount() = dataSet.size

}