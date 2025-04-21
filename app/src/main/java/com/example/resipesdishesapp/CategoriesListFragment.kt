package com.example.resipesdishesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import com.example.resipesdishesapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {

    private var _categoriesBinding: FragmentListCategoriesBinding? = null
    private val categoriesBinding: FragmentListCategoriesBinding
        get() = _categoriesBinding
            ?: throw IllegalStateException("FragmentListCategoriesBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _categoriesBinding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return categoriesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _categoriesBinding = null
    }

    private fun initRecycler() {
        val categoriesAdapter = CategoriesListAdapter(STUB.getCategories())
        categoriesBinding.rvCategories.adapter = categoriesAdapter


        categoriesAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {

            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        }
        )
    }

    fun openRecipesByCategoryId(categoryId:Int) {

        val categoryName = STUB.getCategories().find { it.id == categoryId }?.title
        val categoryImage = STUB.getCategories().find {it.id == categoryId}?.imageUrl

        val bundle = bundleOf(
            ARG_CATEGORY_ID to categoryId,
            ARG_CATEGORY_NAME to categoryName,
            ARG_CATEGORY_IMAGE_URL to categoryImage
        )

        val recipeFragment = RecipesListFragment()
            recipeFragment.arguments = bundle

        parentFragmentManager.commit {
            replace(R.id.mainContainer, recipeFragment)
            addToBackStack(null)
        }
    }
}

const val ARG_CATEGORY_ID = "ARG_CATEGORY_ID"
const val ARG_CATEGORY_NAME = "ARG_CATEGORY_NAME"
const val ARG_CATEGORY_IMAGE_URL = "ARG_CATEGORY_IMAGE_URL"



