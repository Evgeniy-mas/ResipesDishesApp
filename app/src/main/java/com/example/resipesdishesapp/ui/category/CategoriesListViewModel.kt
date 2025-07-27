package com.example.resipesdishesapp.ui.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.resipesdishesapp.data.NetworkResult
import com.example.resipesdishesapp.data.RecipesRepository
import com.example.resipesdishesapp.model.Category
import kotlinx.coroutines.launch

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {

    private val recipesRepository = RecipesRepository()
    private val _categoriesState = MutableLiveData(CategoriesListState())
    val categoriesState: LiveData<CategoriesListState> get() = _categoriesState

    data class CategoriesListState(
        val categories: List<Category> = emptyList(),
        val errorId: Int? = null,
        val imageUrl: String = "https://recipes.androidsprint.ru/api"
    )

    fun loadCategories() {

        viewModelScope.launch {
            when (val result = recipesRepository.getCategories()) {
                is NetworkResult.Success -> {
                    _categoriesState.postValue(
                        CategoriesListState(
                            categories = result.data,
                            errorId = null
                        )
                    )
                }

                is NetworkResult.Error -> {
                    _categoriesState.postValue(
                        CategoriesListState(
                            categories = emptyList(),
                            errorId = result.errorId
                        )
                    )
                }
            }
        }
    }
}