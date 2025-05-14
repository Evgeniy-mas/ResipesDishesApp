package com.example.resipesdishesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.resipesdishesapp.databinding.ItemIngredientBinding

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(ingredient: Ingredient) {
            with(binding) {
                tvIngredient.text = ingredient.description
                tvQuantity.text = ingredient.quantity
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

    }