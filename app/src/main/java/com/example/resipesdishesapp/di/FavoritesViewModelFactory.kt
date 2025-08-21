package com.example.resipesdishesapp.di

import com.example.resipesdishesapp.data.RecipesRepository
import com.example.resipesdishesapp.ui.recipe.favorites.FavoritesViewModel

class FavoritesViewModelFactory(
    private val recipesRepository: RecipesRepository
): Factory<FavoritesViewModel> {
    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(recipesRepository)
    }
}