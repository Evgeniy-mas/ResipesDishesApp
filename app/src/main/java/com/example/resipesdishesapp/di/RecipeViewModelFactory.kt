package com.example.resipesdishesapp.di

import com.example.resipesdishesapp.data.RecipesRepository
import com.example.resipesdishesapp.ui.recipe.listRecipes.RecipesListViewModel
import com.example.resipesdishesapp.ui.recipe.recipe.RecipeViewModel

class RecipeViewModelFactory(
    private val recipesRepository: RecipesRepository
) : Factory<RecipeViewModel> {
    override fun create(): RecipeViewModel {
        return RecipeViewModel(recipesRepository)
    }
}