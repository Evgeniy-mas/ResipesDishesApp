package com.example.resipesdishesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.resipesdishesapp.databinding.ItemIngredientBinding

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    companion object {
        var quantityPortion = 1
    }

    class ViewHolder(private val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredient: Ingredient) {
            val calculatePortion = ingredient.quantity.toDouble() * quantityPortion

            val newQuantity = if (calculatePortion % 1 == 0.0) {
                calculatePortion.toInt().toString()
            } else calculatePortion.toString()

            with(binding) {
                tvIngredient.text = ingredient.description
                tvQuantity.text = newQuantity
                tvUnit.text = ingredient.unitOfMeasure
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemIngredientBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val ingredient = dataSet[position]
        viewHolder.bind(ingredient)
    }

    override fun getItemCount() = dataSet.size

    fun updateIngredients(progress: Int) {
        quantityPortion = progress
    }
}