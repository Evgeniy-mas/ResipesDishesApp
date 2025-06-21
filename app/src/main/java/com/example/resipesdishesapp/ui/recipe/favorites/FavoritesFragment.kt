package com.example.resipesdishesapp.ui.recipe.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.resipesdishesapp.data.KeysConstant
import com.example.resipesdishesapp.R
import com.example.resipesdishesapp.ui.recipe.listRecipes.RecipesListAdapter
import com.example.resipesdishesapp.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {
    private var _favoritesBinding: FragmentFavoritesBinding? = null
    private val favoritesBinding: FragmentFavoritesBinding
        get() = _favoritesBinding
            ?: throw IllegalStateException("FragmentFavoritesBinding must not be null")

    private lateinit var recipesAdapter: RecipesListAdapter
    private val viewModel: FavoritesViewModel by viewModels()

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
        viewModel.loadFavorites()
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

        viewModel.favoritesState.observe(viewLifecycleOwner) { state ->
            recipesAdapter.updateRecipes(state.favoriteRecipes)

            favoritesBinding.tvEmptyFavorites.visibility =
                if (state.isEmpty) View.VISIBLE else View.GONE
            favoritesBinding.rvFavorites.visibility = if (state.isEmpty) View.GONE else View.VISIBLE
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = bundleOf(
            KeysConstant.ARG_RECIPE_ID to recipeId,
        )

        findNavController().navigate(R.id.recipeFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _favoritesBinding = null
    }
}