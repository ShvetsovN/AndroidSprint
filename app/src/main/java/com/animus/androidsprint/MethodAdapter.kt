package com.animus.androidsprint

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.animus.androidsprint.databinding.ItemMethodBinding

class MethodAdapter(private val dataSet: List<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ItemMethodBinding.bind(item)
        fun bind(itemView: Recipe) = with(binding) {
            tvMethod.text = itemView.method[position]
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_method, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val method = dataSet[position]
    }

    override fun getItemCount() = dataSet.size
}