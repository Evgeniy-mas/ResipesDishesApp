package com.example.resipesdishesapp

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.resipesdishesapp.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {

    var startPoint = 1
    var endPoint = 5

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

        _recipeBinding?.ibAddToFavourites?.setImageResource(
            R.drawable.ic_heart_favourites_empty
        )

        val favorites = getFavorites()
        val isFavorite = favorites.contains(recipe.id.toString())

        _recipeBinding?.ibAddToFavourites?.setImageResource(
            if (isFavorite) R.drawable.ic_heart_favourites
            else R.drawable.ic_heart_favourites_empty
        )

        _recipeBinding?.ibAddToFavourites?.setOnClickListener {
            val currentFavorites = getFavorites().toMutableSet()
            val recipeId = recipe.id.toString()


            if (currentFavorites.contains(recipeId)) {
                currentFavorites.remove(recipeId)
                _recipeBinding?.ibAddToFavourites?.setImageResource(
                    R.drawable.ic_heart_favourites_empty
                )
            } else {
                currentFavorites.add(recipeId)
                _recipeBinding?.ibAddToFavourites?.setImageResource(
                    R.drawable.ic_heart_favourites
                )
            }
            saveFavorites(currentFavorites)
        }
    }

    private fun saveFavorites(idRecipe: Set<String>) {
        val sharedPrefs =
            requireContext().getSharedPreferences(KeysConstant.PREFS_SHARED, Context.MODE_PRIVATE)
        sharedPrefs.edit()
            .putStringSet(KeysConstant.FAVORITES_KEY, idRecipe)
            .apply()
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs =
            requireContext().getSharedPreferences(KeysConstant.PREFS_SHARED, Context.MODE_PRIVATE)
        return sharedPrefs.getStringSet(KeysConstant.FAVORITES_KEY, HashSet()) ?: HashSet()
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

        recipeBinding.sbQuantityPortion.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                recipeBinding.tvQuantityPortion.text = "$progress"
                ingredientsAdapter.updateIngredients(progress)
                ingredientsAdapter.notifyDataSetChanged()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    startPoint = seekBar.progress
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    endPoint = seekBar.progress
                }
            }
        })
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