package com.example.resipesdishesapp.ui.recipe.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
                updateFavoriteIcon(state.isFavorite)
                recipeBinding.tvTitle.text = recipe.title

                state.recipeImage?.let { imageUrl ->
                    Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.img_placeholder)
                        .error(R.drawable.img_error)
                        .into(recipeBinding.ivRecipeImage)
                }

                ingredientsAdapter.dataSet = recipe.ingredients
                ingredientsAdapter.quantityPortion = state.portion

                methodAdapter.dataSet = recipe.method

                recipeBinding.tvQuantityPortion.text = state.portion.toString()
                recipeBinding.sbQuantityPortion.progress = state.portion

                state.errorId?.let { resId ->
                    Toast.makeText(requireContext(), resId, Toast.LENGTH_SHORT).show()
                }
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

        viewModel.favoriteUpdated.observe(viewLifecycleOwner) { update ->
            if (update) {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    "favorite_updated", true
                )
            }
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        recipeBinding.ibAddToFavourites.setImageResource(
            if (isFavorite) R.drawable.ic_heart_favourites
            else R.drawable.ic_heart_favourites_empty
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