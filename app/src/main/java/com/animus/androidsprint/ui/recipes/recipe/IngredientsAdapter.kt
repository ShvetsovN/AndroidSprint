package com.animus.androidsprint.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.animus.androidsprint.model.Ingredient
import com.animus.androidsprint.R
import com.animus.androidsprint.databinding.ItemIngredientBinding
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter(var dataSet: List<Ingredient> = listOf()) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var quantity = 1

    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ItemIngredientBinding.bind(item)
        fun bind(itemView: Ingredient, quantity: Int) = with(binding) {
            tvIngredientDescription.text = itemView.description
            val totalQuantity = BigDecimal(itemView.quantity) * BigDecimal(quantity)
            val displayQuantity: String = totalQuantity
                .setScale(1, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString()
            tvIngredientQuantity.text = "$displayQuantity ${itemView.unitOfMeasure}"
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
        viewHolder.bind(ingredient, quantity)
    }

    override fun getItemCount() = dataSet.size
}