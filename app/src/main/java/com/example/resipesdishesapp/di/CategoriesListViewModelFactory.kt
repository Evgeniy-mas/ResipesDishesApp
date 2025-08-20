package com.example.resipesdishesapp.di

import com.example.resipesdishesapp.data.RecipesRepository
import com.example.resipesdishesapp.ui.category.CategoriesListViewModel

class CategoriesListViewModelFactory(
    private val recipesRepository: RecipesRepository
) : Factory<CategoriesListViewModel> {

    override fun create(): CategoriesListViewModel {
        return CategoriesListViewModel(recipesRepository)
    }
}