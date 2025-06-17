package com.example.resipesdishesapp.ui.recipe.listRecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.resipesdishesapp.data.KeysConstant
import com.example.resipesdishesapp.R
import com.example.resipesdishesapp.databinding.FragmentRecipesListBinding
import com.example.resipesdishesapp.ui.recipe.recipe.RecipeFragment

class RecipesListFragment : Fragment() {

    private val viewModel: RecipesListViewModel by viewModels()
    private var _recipesBinding: FragmentRecipesListBinding? = null
    private val recipesBinding: FragmentRecipesListBinding
        get() = _recipesBinding
            ?: throw IllegalStateException("RecipesListFragment must not be null")

    private lateinit var recipesAdapter: RecipesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _recipesBinding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return recipesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryId = arguments?.getInt(KeysConstant.ARG_CATEGORY_ID) ?: 0
        val categoryName = arguments?.getString(KeysConstant.ARG_CATEGORY_NAME)
        val categoryImageUrl = arguments?.getString(KeysConstant.ARG_CATEGORY_IMAGE_URL)

        initRecycler()
        viewModel.loadData(categoryId, categoryName, categoryImageUrl)
    }

    private fun initRecycler() {
        recipesAdapter = RecipesListAdapter(emptyList()).apply {
            setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            })
        }
        recipesBinding.rvRecipes.adapter = recipesAdapter

        viewModel.recipesListState.observe(viewLifecycleOwner) { state ->
            state.categoryName?.let { recipesBinding.tvTitle.text = it }
            recipesBinding.ivCategoryImage.setImageDrawable(state.categoryImage)
            recipesAdapter.updateRecipes(state.recipes)
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = bundleOf(KeysConstant.ARG_RECIPE_ID to recipeId)
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _recipesBinding = null
    }
}