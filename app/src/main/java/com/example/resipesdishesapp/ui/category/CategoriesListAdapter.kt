package com.example.resipesdishesapp.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.resipesdishesapp.model.Category
import com.example.resipesdishesapp.R
import com.example.resipesdishesapp.databinding.ItemCategoryBinding

class CategoriesListAdapter(
    private var dataSet: List<Category>,
) : RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null
    private var categoryImageUrl: String = ""

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    fun updateCategories(categories: List<Category>, imageUrl: String = "") {
        if (imageUrl.isNotEmpty()) {
            this.categoryImageUrl = imageUrl
        }
        this.dataSet = categories
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category, baseImageUrl: String) {
            with(binding) {
                tvTitle.text = category.title
                tvDescription.text = category.description
                ivCategoryImage.contentDescription = root.context.getString(
                    R.string.category_image,
                    category.title
                )

                Glide.with(root.context)
                    .load("$baseImageUrl/images/${category.imageUrl}")
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_error)
                    .centerCrop()
                    .into(ivCategoryImage)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val category = dataSet[position]
        viewHolder.bind(category, categoryImageUrl)
        viewHolder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(category.id)
        }
    }

    override fun getItemCount() = dataSet.size
}
