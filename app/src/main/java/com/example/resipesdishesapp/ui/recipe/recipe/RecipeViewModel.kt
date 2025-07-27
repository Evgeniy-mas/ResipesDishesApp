package com.example.resipesdishesapp.ui.recipe.recipe

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.resipesdishesapp.data.KeysConstant
import com.example.resipesdishesapp.data.NetworkResult
import com.example.resipesdishesapp.data.RecipesRepository
import com.example.resipesdishesapp.model.Recipe

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> get() = _recipeState
    private val recipesRepository = RecipesRepository()
    private val recipesImageUrl = "https://recipes.androidsprint.ru/api/images/"

    data class RecipeState(
        val recipe: Recipe? = null,
        val recipeImage: String? = null,
        val portion: Int = 1,
        val isFavorite: Boolean = false,
        val errorId: Int? = null
    )

    fun loadRecipe(recipeId: Int) {
        val favorites = getFavorites()
        val isFavorite = favorites.contains(recipeId.toString())

        recipesRepository.getRecipeById(recipeId) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    val recipeUrl = result.data.copy(
                        imageUrl = "$recipesImageUrl${result.data.imageUrl}"
                    )
                    _recipeState.postValue(
                        RecipeState(
                            recipe = result.data,
                            recipeImage = recipeUrl.imageUrl,
                            isFavorite = isFavorite,
                            portion = 1
                        )
                    )
                }

                is NetworkResult.Error -> {
                    _recipeState.postValue(
                        RecipeState(

                            errorId = result.errorId
                        )
                    )
                }
            }
        }
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

    fun updatePortion(portion: Int) {
        _recipeState.value = recipeState.value?.copy(portion = portion)
    }
}