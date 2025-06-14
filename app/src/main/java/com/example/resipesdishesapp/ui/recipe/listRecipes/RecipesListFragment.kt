package com.example.resipesdishesapp.ui.recipe.listRecipes

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.resipesdishesapp.data.KeysConstant
import com.example.resipesdishesapp.R
import com.example.resipesdishesapp.data.STUB
import com.example.resipesdishesapp.databinding.FragmentRecipesListBinding
import com.example.resipesdishesapp.ui.recipe.recipe.RecipeFragment

class RecipesListFragment : Fragment() {

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

    private var _recipesBinding: FragmentRecipesListBinding? = null
    private val recipesBinding: FragmentRecipesListBinding
        get() = _recipesBinding
            ?: throw IllegalStateException("RecipesListFragment must not be null")

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
        initBundleData()
        initUI()
        initRecycler()
    }

    private fun initBundleData() {
        arguments?.let { bundle ->
            categoryId = bundle.getInt(KeysConstant.ARG_CATEGORY_ID)
            categoryName = bundle.getString(KeysConstant.ARG_CATEGORY_NAME)
            categoryImageUrl = bundle.getString(KeysConstant.ARG_CATEGORY_IMAGE_URL)
        }
    }

    private fun initUI() {
        _recipesBinding?.tvTitle?.text = categoryName

        val drawable =
            try {
                Drawable.createFromStream(
                    categoryImageUrl?.let { requireContext().assets.open(it) },
                    null
                )
            } catch (e: Exception) {
                val errorMessage = requireContext().getString(
                    R.string.drawable_error
                )
                Log.e("!!!", "$errorMessage $categoryImageUrl", e)
                null
            }
        _recipesBinding?.ivCategoryImage?.setImageDrawable(drawable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _recipesBinding = null
    }

    private fun initRecycler() {
        val recipesAdapter = RecipesListAdapter(STUB.getRecipesByCategoryId(categoryId ?: 0))
        recipesBinding.rvRecipes.adapter = recipesAdapter

        recipesAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        }
        )
    }

    fun openRecipeByRecipeId(recipeId: Int) {

        val bundle = bundleOf(
            KeysConstant.ARG_RECIPE_ID to recipeId
        )

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            addToBackStack(null)
        }
    }
}