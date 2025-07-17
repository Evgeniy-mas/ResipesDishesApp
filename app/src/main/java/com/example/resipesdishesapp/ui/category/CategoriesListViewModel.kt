package com.example.resipesdishesapp.ui.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.resipesdishesapp.data.RecipesRepository
import com.example.resipesdishesapp.model.Category

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {

    private val recipesRepository = RecipesRepository(application.applicationContext)

    private val _categoriesState = MutableLiveData(CategoriesListState())
    val categoriesState: LiveData<CategoriesListState> get() = _categoriesState

    data class CategoriesListState(
        val categories: List<Category> = emptyList()
    )

    fun loadCategories() {
        recipesRepository.getCategories { categories ->

            _categoriesState.postValue(
                CategoriesListState(
                    categories = categories
                )
            )
        }
    }
}