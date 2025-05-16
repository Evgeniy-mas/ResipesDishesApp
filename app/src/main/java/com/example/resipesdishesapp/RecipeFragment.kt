package com.example.resipesdishesapp

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.resipesdishesapp.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {

    private var recipeImageUrl: String? = null
    private lateinit var recipe: Recipe

    private var _recipeBinding: FragmentRecipeBinding? = null
    private val recipeBinding: FragmentRecipeBinding
        get() = _recipeBinding
            ?: throw IllegalStateException("RecipeFragment must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _recipeBinding = FragmentRecipeBinding.inflate(inflater, container, false)
        return recipeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBundleData()
        initUI()
        initRecyclerIngredients()
        initRecyclerMethods()
    }

    private fun initBundleData() {
        recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(KeysConstant.ARG_RECIPE, Recipe::class.java)
        } else {
            arguments?.getParcelable(KeysConstant.ARG_RECIPE)
        } ?: throw IllegalStateException("Recipe not found in arguments")
    }

    private fun initUI() {

        recipeImageUrl = recipe.imageUrlHeader
        _recipeBinding?.tvTitle?.text = recipe.title

        val drawable =
            try {
                Drawable.createFromStream(
                    recipeImageUrl?.let { requireContext().assets.open(it) },
                    null
                )
            } catch (e: Exception) {
                val errorMessage = requireContext().getString(
                    R.string.drawable_error
                )
                Log.e("!!!", "$errorMessage $recipeImageUrl", e)
                null
            }

        _recipeBinding?.ivRecipeImage?.setImageDrawable(drawable)
    }

    private fun initRecyclerIngredients() {
        val ingredientsAdapter = IngredientsAdapter(recipe.ingredients)
        recipeBinding.rvIngredients.adapter = ingredientsAdapter
        val divider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        divider.dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.main_space_half_8)
        divider.dividerInsetStart = resources.getDimensionPixelSize(R.dimen.main_space_half_8)
        divider.isLastItemDecorated = false
        divider.dividerColor = ContextCompat.getColor(requireContext(), R.color.divider)
        divider.dividerThickness = resources.getDimensionPixelSize(R.dimen.divider)
        recipeBinding.rvIngredients.addItemDecoration(divider)
    }

    private fun initRecyclerMethods() {
        val methodAdapter = MethodAdapter(recipe.method)
        recipeBinding.rvMethod.adapter = methodAdapter
        val divider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        divider.dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.main_space_half_8)
        divider.dividerInsetStart = resources.getDimensionPixelSize(R.dimen.main_space_half_8)
        divider.isLastItemDecorated = false
        divider.dividerColor = ContextCompat.getColor(requireContext(), R.color.divider)
        divider.dividerThickness = resources.getDimensionPixelSize(R.dimen.divider)
        recipeBinding.rvMethod.addItemDecoration(divider)
    }
}