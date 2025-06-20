package com.example.resipesdishesapp.ui.recipe.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.resipesdishesapp.databinding.ItemMethodBinding

class MethodAdapter :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    var dataSet: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(private val binding: ItemMethodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(method: String, index: Int) {

            with(binding) {
                tvMethod.text = method
                "${index + 1}.".also { tvIndex.text = it }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMethodBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val method = dataSet[position]
        viewHolder.bind(method, position)
    }

    override fun getItemCount() = dataSet.size
}