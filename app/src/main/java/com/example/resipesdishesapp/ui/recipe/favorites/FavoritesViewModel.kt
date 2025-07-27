package com.example.resipesdishesapp.ui.recipe.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.resipesdishesapp.data.KeysConstant
import com.example.resipesdishesapp.data.NetworkResult
import com.example.resipesdishesapp.data.RecipesRepository
import com.example.resipesdishesapp.model.Recipe

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val _favoritesState = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> get() = _favoritesState
    private val recipesRepository = RecipesRepository()
    private val recipesImageUrl = "https://recipes.androidsprint.ru/api/images/"

    data class FavoritesState(
        val favoriteRecipes: List<Recipe> = emptyList(),
        val isEmpty: Boolean = true,
        val errorId: Int? = null
    )

    fun loadFavorites() {
        val favoriteIds = getFavorites().mapNotNull { it.toIntOrNull() }.toSet()

        if (favoriteIds.isEmpty()) {
            _favoritesState.value = FavoritesState(
                favoriteRecipes = emptyList(),
                isEmpty = true,
                errorId = null
            )
            return
        }

        recipesRepository.getListRecipeId(favoriteIds) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    val recipesUrl = result.data.map { recipe ->
                        recipe.copy(imageUrl = "$recipesImageUrl${recipe.imageUrl}")
                    }
                    _favoritesState.postValue(
                        FavoritesState(
                            favoriteRecipes = recipesUrl,
                            isEmpty = recipesUrl.isEmpty(),
                            errorId = null
                        )
                    )
                }

                is NetworkResult.Error -> {
                    _favoritesState.postValue(
                        FavoritesState(
                            favoriteRecipes = emptyList(),
                            isEmpty = true,
                            errorId = result.errorId
                        )
                    )
                }
            }
        }
    }

    private fun getFavorites(): Set<String> {
        val sharedPrefs = getApplication<Application>().getSharedPreferences(
            KeysConstant.PREFS_SHARED,
            Context.MODE_PRIVATE
        )
        return sharedPrefs.getStringSet(KeysConstant.FAVORITES_KEY, emptySet()) ?: emptySet()
    }
}