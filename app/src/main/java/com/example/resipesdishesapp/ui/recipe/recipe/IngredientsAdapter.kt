package com.example.resipesdishesapp.ui.recipe.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.resipesdishesapp.model.Ingredient
import com.example.resipesdishesapp.databinding.ItemIngredientBinding

class IngredientsAdapter :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    var dataSet: List<Ingredient> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var quantityPortion: Int = 1
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(private val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredient: Ingredient, quantityPortion: Int) {
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
        viewHolder.bind(dataSet[position], quantityPortion)
    }

    override fun getItemCount() = dataSet.size
}