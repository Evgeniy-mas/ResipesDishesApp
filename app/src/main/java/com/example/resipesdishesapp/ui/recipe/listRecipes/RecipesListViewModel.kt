package com.example.resipesdishesapp.ui.recipe.listRecipes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.resipesdishesapp.data.NetworkResult
import com.example.resipesdishesapp.data.RecipesRepository
import com.example.resipesdishesapp.model.Recipe

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    private val _recipesListState = MutableLiveData(RecipesListState())
    val recipesListState: LiveData<RecipesListState> get() = _recipesListState
    private val recipesRepository = RecipesRepository()

    private val recipesImageUrl = "https://recipes.androidsprint.ru/api/images/"

    data class RecipesListState(
        val categoryName: String? = null,
        val categoryImageUrl: String? = null,
        val recipes: List<Recipe> = emptyList(),
        val errorId: Int? = null
    )

    fun loadData(categoryId: Int, categoryName: String?, categoryImageName: String?) {
        
        val fullImageUrl = if (categoryImageName != null) {
            "$recipesImageUrl$categoryImageName"
        } else {
            null
        }

        _recipesListState.value = RecipesListState(
            categoryName = categoryName,
            categoryImageUrl = fullImageUrl,
            recipes = emptyList()
        )

        recipesRepository.getRecipesCategoryId(categoryId) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    _recipesListState.postValue(
                        RecipesListState(
                            categoryName = categoryName,
                            categoryImageUrl = fullImageUrl,
                            recipes = result.data.map {
                                it.copy(imageUrl = "$recipesImageUrl${it.imageUrl}")
                            }
                        )
                    )
                }

                is NetworkResult.Error -> {
                    _recipesListState.postValue(
                        RecipesListState(
                            categoryName = categoryName,
                            categoryImageUrl = fullImageUrl,
                            recipes = emptyList(),
                            errorId = result.errorId
                        )
                    )
                }
            }
        }
    }
}