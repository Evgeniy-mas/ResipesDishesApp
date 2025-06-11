package com.example.resipesdishesapp.ui.recipe.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
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
        val recipeImage: Drawable? = null,
        val portion: Int = 1,
        val isFavorite: Boolean = false
    )

    fun loadRecipe(recipeId: Int) {
        // TODO("Load from network")
        val recipe = STUB.getRecipeById(recipeId)
        val favorites = getFavorites()
        val isFavorite = favorites.contains(recipeId.toString())

        val drawable = try {
            val inputStream = getApplication<Application>().assets.open(recipe.imageUrlHeader)
            Drawable.createFromStream(inputStream, null)
        } catch (e: Exception) {
            Log.e("RecipeViewModel", "Error loading image", e)
            null
        }

        _recipeState.value = _recipeState.value?.copy(
            recipe = recipe,
            isFavorite = isFavorite,
            portion = 1,
            recipeImage = drawable
        )
    }

    fun onFavoritesClicked() {
        _recipeState.value?.let { currentState ->
            currentState.recipe?.id?.toString()?.let { recipeId ->
                val favorites = getFavorites().toMutableSet()
                val isFavorite = !currentState.isFavorite

                if (isFavorite) {
                    favorites.add(recipeId)
                } else {
                    favorites.remove(recipeId)
                }

                saveFavorites(favorites)
                _recipeState.value = currentState.copy(isFavorite = isFavorite)
            }
        }
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