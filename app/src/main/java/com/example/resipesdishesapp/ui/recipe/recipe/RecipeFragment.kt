package com.example.resipesdishesapp.ui.recipe.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.resipesdishesapp.R
import com.example.resipesdishesapp.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels()

    private var _recipeBinding: FragmentRecipeBinding? = null
    private val recipeBinding: FragmentRecipeBinding
        get() = _recipeBinding
            ?: throw IllegalStateException("RecipeFragment must not be null")

    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var methodAdapter: MethodAdapter

    private val args: RecipeFragmentArgs by navArgs()

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

        initAdapters()
        viewModel.loadRecipe(args.recipeId)
        initUI()
    }

    private fun initAdapters() {

        ingredientsAdapter = IngredientsAdapter()
        methodAdapter = MethodAdapter()

        recipeBinding.rvIngredients.adapter = ingredientsAdapter
        recipeBinding.rvMethod.adapter = methodAdapter

        recyclerViewDivider(recipeBinding.rvIngredients)
        recyclerViewDivider(recipeBinding.rvMethod)
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

                ingredientsAdapter.dataSet = recipe.ingredients
                ingredientsAdapter.quantityPortion = state.portion

                methodAdapter.dataSet = recipe.method

                recipeBinding.tvQuantityPortion.text = state.portion.toString()
                recipeBinding.sbQuantityPortion.progress = state.portion
            }
        }

        recipeBinding.ibAddToFavourites.setOnClickListener {
            viewModel.onFavoritesClicked()
        }

        recipeBinding.sbQuantityPortion.setOnSeekBarChangeListener(
            PortionSeekBarListener { progress ->
                recipeBinding.tvQuantityPortion.text = "$progress"
                viewModel.updatePortion(progress)
            }
        )
    }

    private fun recyclerViewDivider(recyclerView: RecyclerView) {
        val divider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        divider.dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.main_space_half_8)
        divider.dividerInsetStart = resources.getDimensionPixelSize(R.dimen.main_space_half_8)
        divider.isLastItemDecorated = false
        divider.dividerColor = ContextCompat.getColor(requireContext(), R.color.divider)
        divider.dividerThickness = resources.getDimensionPixelSize(R.dimen.divider)
        recyclerView.addItemDecoration(divider)
    }
}

class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) :
    SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        onChangeIngredients(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}