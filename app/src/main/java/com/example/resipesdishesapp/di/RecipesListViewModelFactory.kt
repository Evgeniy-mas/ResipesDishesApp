package com.example.resipesdishesapp.di

import com.example.resipesdishesapp.data.RecipesRepository
import com.example.resipesdishesapp.ui.recipe.listRecipes.RecipesListViewModel

class RecipesListViewModelFactory(
    private val recipesRepository: RecipesRepository
): Factory<RecipesListViewModel> {
    override fun create(): RecipesListViewModel {
        return RecipesListViewModel(recipesRepository)
    }
}