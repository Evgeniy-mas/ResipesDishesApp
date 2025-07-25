package com.example.resipesdishesapp.ui.recipe.listRecipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.resipesdishesapp.R
import com.example.resipesdishesapp.databinding.ItemRecipesBinding
import com.example.resipesdishesapp.model.Recipe

class RecipesListAdapter(private var dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    fun updateRecipes(newRecipes: List<Recipe>) {
        dataSet = newRecipes
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemRecipesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipes: Recipe) {
            with(binding) {
                tvTitle.text = recipes.title

                ivRecipeImage.contentDescription = root.context.getString(
                    R.string.recipe_image_description,
                    recipes.title
                )

                Glide.with(root.context)
                    .load(recipes.imageUrl)
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_error)
                    .into(ivRecipeImage)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecipesBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewholder: ViewHolder, position: Int) {
        val recipes = dataSet[position]
        viewholder.bind(recipes)
        viewholder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(recipes.id)
        }
    }

    override fun getItemCount() = dataSet.size
}