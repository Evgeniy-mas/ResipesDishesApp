package com.example.resipesdishesapp.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.resipesdishesapp.data.KeysConstant
import com.example.resipesdishesapp.R
import com.example.resipesdishesapp.ui.recipe.listRecipes.RecipesListFragment
import com.example.resipesdishesapp.data.STUB
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

    fun openRecipesByCategoryId(categoryId: Int) {
        val category = STUB.getCategories().find { it.id == categoryId }

        val bundle = bundleOf(
            KeysConstant.ARG_CATEGORY_ID to categoryId,
            KeysConstant.ARG_CATEGORY_NAME to category?.title,
            KeysConstant.ARG_CATEGORY_IMAGE_URL to category?.imageUrl
        )

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipesListFragment>(R.id.mainContainer, args = bundle)
            addToBackStack(null)
        }
    }
}





