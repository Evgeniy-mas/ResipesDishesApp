package com.example.resipesdishesapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.resipesdishesapp.databinding.ItemCategoryBinding

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            with(binding) {
                tvTitle.text = category.title
                tvDescription.text = category.description

                val drawable =
                    try {
                        Drawable.createFromStream(
                            root.context.assets.open(category.imageUrl),
                            null
                        )
                    } catch (e: Exception) {
                        val errorMessage = root.context.getString(
                            R.string.drawable_error
                        )
                        Log.e("!!!", "$errorMessage ${category.imageUrl}", e)
                        null
                    }
                ivCategoryImage.setImageDrawable(drawable)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

}