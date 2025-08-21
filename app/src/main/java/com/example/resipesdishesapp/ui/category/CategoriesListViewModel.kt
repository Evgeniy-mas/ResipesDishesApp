package com.example.resipesdishesapp.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resipesdishesapp.data.NetworkResult
import com.example.resipesdishesapp.data.RecipesRepository
import com.example.resipesdishesapp.model.Category
import kotlinx.coroutines.launch

class CategoriesListViewModel(
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    private val _categoriesState = MutableLiveData(CategoriesListState())
    val categoriesState: LiveData<CategoriesListState> get() = _categoriesState

    data class CategoriesListState(
        val categories: List<Category> = emptyList(),
        val errorId: Int? = null,
        val imageUrl: String = "https://recipes.androidsprint.ru/api"
    )

    fun loadCategories() {
        viewModelScope.launch {
            val cachedCategories = recipesRepository.getCategoriesFromCache()
            if (cachedCategories.isNotEmpty()) {
                _categoriesState.postValue(
                    CategoriesListState(
                        categories = cachedCategories,
                        errorId = null
                    )
                )
            }

            when (val result = recipesRepository.getCategories()) {
                is NetworkResult.Success -> {
                    recipesRepository.saveCategoriesToCache(result.data)
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
                            categories = cachedCategories.takeIf { it.isNotEmpty() } ?: emptyList(),
                            errorId = if (cachedCategories.isEmpty()) result.errorId else null
                        )
                    )
                }
            }
        }
    }
}