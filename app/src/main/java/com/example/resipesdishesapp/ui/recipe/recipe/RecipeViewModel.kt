package com.example.resipesdishesapp.ui.recipe.recipe

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.resipesdishesapp.data.NetworkResult
import com.example.resipesdishesapp.data.RecipesRepository
import com.example.resipesdishesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> get() = _recipeState
    private val recipesRepository = RecipesRepository(application.applicationContext)
    private val recipesImageUrl = "https://recipes.androidsprint.ru/api/images/"

    data class RecipeState(
        val recipe: Recipe? = null,
        val recipeImage: String? = null,
        val portion: Int = 1,
        val isFavorite: Boolean = false,
        val errorId: Int? = null
    )

    fun loadRecipe(recipeId: Int) {

        viewModelScope.launch {
            when (val result = recipesRepository.getRecipeById(recipeId)) {
                is NetworkResult.Success -> {
                    val isFavorite = result.data.isFavorite
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
            currentState.recipe?.let { recipe ->
                viewModelScope.launch {
                    val newFavoriteStatus = !currentState.isFavorite
                    recipesRepository.setFavorite(recipe.id, newFavoriteStatus)
                    _recipeState.value = currentState.copy(isFavorite = newFavoriteStatus)
                }
            }
        }
    }

    fun updatePortion(portion: Int) {
        _recipeState.value = recipeState.value?.copy(portion = portion)
    }
}