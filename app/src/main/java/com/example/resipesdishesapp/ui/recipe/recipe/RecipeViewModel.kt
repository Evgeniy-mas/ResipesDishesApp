package com.example.resipesdishesapp.ui.recipe.recipe

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.resipesdishesapp.data.KeysConstant
import com.example.resipesdishesapp.data.STUB
import com.example.resipesdishesapp.model.Recipe

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> get() = _recipeState

    data class RecipeState(
        val recipe: Recipe? = null,
        val imageUrl: String = "",
        val portion: Int = 1,
        val isFavorite: Boolean = false
    )

    fun loadRecipe(recipeId: Int) {
        // TODO("Load from network")
        val recipe = STUB.getRecipeById(recipeId)
        val favorites = getFavorites()
        val isFavorite = favorites.contains(recipeId.toString())

        _recipeState.value = RecipeState(
            recipe = recipe,
            portion = 1,
            isFavorite = isFavorite
        )
    }

    fun onFavoritesClicked() {
        val currentState = _recipeState.value
        val recipeId = currentState?.recipe?.id?.toString()
        val favorites = getFavorites().toMutableSet()

        val isFavorite = !currentState?.isFavorite!!
        if (isFavorite) {
            if (recipeId != null) {
                favorites.add(recipeId)
            }
        } else {
            favorites.remove(recipeId)
        }

        saveFavorites(favorites)
        _recipeState.value = currentState.copy(isFavorite = isFavorite)
    }

    private fun saveFavorites(idRecipe: Set<String>) {
        val sharedPrefs =
            getApplication<Application>().getSharedPreferences(
                KeysConstant.PREFS_SHARED,
                Context.MODE_PRIVATE
            )
        sharedPrefs.edit()
            .putStringSet(KeysConstant.FAVORITES_KEY, idRecipe)
            .apply()
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs = getApplication<Application>().getSharedPreferences(
            KeysConstant.PREFS_SHARED,
            Context.MODE_PRIVATE
        )
        return sharedPrefs.getStringSet(KeysConstant.FAVORITES_KEY, HashSet()) ?: HashSet()
    }
}