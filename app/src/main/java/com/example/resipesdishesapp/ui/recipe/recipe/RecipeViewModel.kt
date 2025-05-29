package com.example.resipesdishesapp.ui.recipe.recipe

import androidx.lifecycle.ViewModel
import com.example.resipesdishesapp.model.Recipe

class RecipeViewModel : ViewModel() {


    data class RecipeState(
        val recipe: Recipe? = null,
        val imageUrl: String = "",
        val portion: Int = 1
    )


}