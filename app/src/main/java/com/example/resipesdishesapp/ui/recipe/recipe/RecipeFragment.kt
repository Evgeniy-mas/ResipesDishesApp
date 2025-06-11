package com.example.resipesdishesapp.ui.recipe.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.resipesdishesapp.data.KeysConstant
import com.example.resipesdishesapp.R
import com.example.resipesdishesapp.model.Recipe
import com.example.resipesdishesapp.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels()

    var startPoint = 1
    var endPoint = 5

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
        val recipeId = arguments?.getInt(KeysConstant.ARG_RECIPE_ID)
            ?: throw IllegalStateException("ID recipe not find")
        viewModel.loadRecipe(recipeId)
        initUI()
    }

    private fun initUI() {
        viewModel.recipeState.observe(viewLifecycleOwner) { state ->
            state.recipe?.let { recipe ->

                recipeBinding.tvTitle.text = recipe.title

                recipeBinding.ivRecipeImage.setImageDrawable(state.recipeImage)

                recipeBinding.ibAddToFavourites.setImageResource(
                    if (state.isFavorite) R.drawable.ic_heart_favourites
                    else R.drawable.ic_heart_favourites_empty
                )

                initRecyclerIngredients(recipe)
                initRecyclerMethods(recipe)
            }
        }

        recipeBinding.ibAddToFavourites.setOnClickListener {
            viewModel.onFavoritesClicked()
        }

        recipeBinding.sbQuantityPortion.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                recipeBinding.tvQuantityPortion.text = "$progress"
                (recipeBinding.rvIngredients.adapter as? IngredientsAdapter)?.updateIngredients(
                    progress
                )
                recipeBinding.rvIngredients.adapter?.notifyDataSetChanged()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                seekBar?.progress?.let { startPoint = it }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.progress?.let { endPoint = it }
            }
        })
    }

    private fun initRecyclerIngredients(recipe: Recipe) {
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

    private fun initRecyclerMethods(recipe: Recipe) {
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