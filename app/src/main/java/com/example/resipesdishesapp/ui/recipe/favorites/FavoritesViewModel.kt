package com.example.resipesdishesapp.ui.recipe.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.resipesdishesapp.data.KeysConstant
import com.example.resipesdishesapp.data.STUB
import com.example.resipesdishesapp.model.Recipe

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val _favoritesState = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> get() = _favoritesState

    data class FavoritesState(
        val favoriteRecipes: List<Recipe> = emptyList(),
        val isEmpty: Boolean = true
    )

    fun loadFavorites() {
        val favoriteIds = getFavorites().map { it.toInt() }.toSet()
        val recipes = STUB.getRecipesByIds(favoriteIds)

        _favoritesState.value = FavoritesState(
            favoriteRecipes = recipes,
            isEmpty = recipes.isEmpty()
        )
    }

    private fun getFavorites(): Set<String> {
        val sharedPrefs = getApplication<Application>().getSharedPreferences(
            KeysConstant.PREFS_SHARED,
            Context.MODE_PRIVATE
        )
        return sharedPrefs.getStringSet(KeysConstant.FAVORITES_KEY, emptySet()) ?: emptySet()
    }
}