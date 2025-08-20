package com.example.resipesdishesapp.ui.recipe.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resipesdishesapp.R
import com.example.resipesdishesapp.data.RecipesRepository
import com.example.resipesdishesapp.model.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel(  private val recipesRepository: RecipesRepository) : ViewModel() {

    private val _favoritesState = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> get() = _favoritesState

    private val recipesImageUrl = "https://recipes.androidsprint.ru/api/images/"

    data class FavoritesState(
        val favoriteRecipes: List<Recipe> = emptyList(),
        val isEmpty: Boolean = true,
        val errorId: Int? = null
    )

    fun loadFavorites() {
        viewModelScope.launch {
            try {
                val favorites = recipesRepository.getFavoriteRecipes()

                val recipesWithUrls = favorites.map {
                    it.copy(imageUrl = "$recipesImageUrl${it.imageUrl}")
                }

                _favoritesState.postValue(
                    FavoritesState(
                        favoriteRecipes = recipesWithUrls,
                        isEmpty = favorites.isEmpty(),
                        errorId = null
                    )
                )
            } catch (e: Exception) {
                _favoritesState.postValue(
                    FavoritesState(
                        favoriteRecipes = emptyList(),
                        isEmpty = true,
                        errorId = R.string.error_loading_favorites
                    )
                )
            }
        }
    }
}