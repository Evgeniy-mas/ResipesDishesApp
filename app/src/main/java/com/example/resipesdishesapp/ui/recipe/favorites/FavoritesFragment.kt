package com.example.resipesdishesapp.ui.recipe.favorites

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import com.example.resipesdishesapp.data.KeysConstant
import com.example.resipesdishesapp.R
import com.example.resipesdishesapp.ui.recipe.recipe.RecipeFragment
import com.example.resipesdishesapp.ui.recipe.listRecipes.RecipesListAdapter
import com.example.resipesdishesapp.data.STUB
import com.example.resipesdishesapp.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {
    private var _favoritesBinding: FragmentFavoritesBinding? = null
    private val favoritesBinding: FragmentFavoritesBinding
        get() = _favoritesBinding
            ?: throw IllegalStateException("FragmentFavoritesBinding must not be null")

    private lateinit var recipesAdapter: RecipesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _favoritesBinding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return favoritesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        loadFavorites()
    }

    private fun initRecycler() {
        recipesAdapter = RecipesListAdapter(emptyList()).apply {
            setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            })
        }

        favoritesBinding.rvFavorites.adapter = recipesAdapter
    }

    private fun loadFavorites() {
        val favoriteIds = getFavorites().map { it.toInt() }.toSet()
        val favoriteRecipes = STUB.getRecipesByIds(favoriteIds)
        recipesAdapter.updateRecipes(favoriteRecipes)

        if (favoriteRecipes.isEmpty()) {
            favoritesBinding.tvEmptyFavorites.visibility = View.VISIBLE
            favoritesBinding.rvFavorites.visibility = View.GONE
        } else {
            favoritesBinding.tvEmptyFavorites.visibility = View.GONE
        }
    }

    private fun getFavorites(): Set<String> {
        val sharedPrefs = requireContext().getSharedPreferences(
            KeysConstant.PREFS_SHARED,
            Context.MODE_PRIVATE
        )
        return sharedPrefs.getStringSet(KeysConstant.FAVORITES_KEY, emptySet()) ?: emptySet()
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        val bundle = bundleOf(
            KeysConstant.ARG_RECIPE to recipe,
            )

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.mainContainer, RecipeFragment::class.java, bundle)
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _favoritesBinding = null
    }
}