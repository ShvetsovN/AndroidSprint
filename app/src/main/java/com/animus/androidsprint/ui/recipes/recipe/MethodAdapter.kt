package com.animus.androidsprint.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.animus.androidsprint.R
import com.animus.androidsprint.databinding.ItemMethodBinding

class MethodAdapter(var dataSet: List<String> = listOf()) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ItemMethodBinding.bind(item)
        fun bind(method: String) = with(binding) {
            tvMethod.text = method
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_method, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val method = dataSet[position]
        viewHolder.bind(method)
    }

    override fun getItemCount() = dataSet.size
}