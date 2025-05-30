package com.example.resipesdishesapp.ui.recipe.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.resipesdishesapp.model.Recipe

class RecipeViewModel : ViewModel() {

    private val _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> get() = _recipeState


    data class RecipeState(
        val recipe: Recipe? = null,
        val imageUrl: String = "",
        val portion: Int = 1,
        val isFavorite: Boolean = false
    )

    init {
        Log.i("!!!", "View")
        _recipeState.value = RecipeState(isFavorite = false)
    }

    fun statusFavorite(isFavorite: Boolean) {
        _recipeState.value = _recipeState.value?.copy(isFavorite = isFavorite)
    }
}