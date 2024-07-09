package com.animus.androidsprint

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.animus.androidsprint.databinding.ItemIngredientBinding
import java.math.BigDecimal

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
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
            val numberOfPortion = (itemView.quantity.toDouble() * quantity).toBigDecimal()
            val quantityText: String = if (numberOfPortion.divide(BigDecimal.ONE).equals(0.0)) {
                "${numberOfPortion.toInt()}"
            } else {
                "${numberOfPortion.setScale(1)}"
            }
            tvIngredientQuantity.text = "$quantityText ${itemView.unitOfMeasure}"
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