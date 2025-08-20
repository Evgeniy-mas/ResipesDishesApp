package com.example.resipesdishesapp.ui.recipe.listRecipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resipesdishesapp.data.NetworkResult
import com.example.resipesdishesapp.data.RecipesRepository
import com.example.resipesdishesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipesListViewModel(  private val recipesRepository: RecipesRepository) : ViewModel() {

    private val _recipesListState = MutableLiveData(RecipesListState())
    val recipesListState: LiveData<RecipesListState> get() = _recipesListState


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

        viewModelScope.launch {
            val cachedRecipes = recipesRepository.getRecipesFromCache(categoryId)
            if (cachedRecipes.isNotEmpty()) {
                _recipesListState.postValue(
                    RecipesListState(
                        categoryName = categoryName,
                        categoryImageUrl = fullImageUrl,
                        recipes = cachedRecipes.map {
                            it.copy(imageUrl = "$recipesImageUrl${it.imageUrl}")
                        }
                    )
                )
            }

            when (val result = recipesRepository.getRecipesCategoryId(categoryId)) {
                is NetworkResult.Success -> {
                    val recipesWithCategory = result.data.map {
                        it.copy(categoryId = categoryId)
                    }
                    recipesRepository.saveRecipesToCache(recipesWithCategory)

                    val recipesUrl = result.data.map { recipe ->
                        recipe.copy(
                            imageUrl = "$recipesImageUrl${recipe.imageUrl}",
                            categoryId = categoryId
                        )
                    }

                    _recipesListState.postValue(
                        RecipesListState(
                            categoryName = categoryName,
                            categoryImageUrl = fullImageUrl,
                            recipes = recipesUrl
                        )
                    )
                }

                is NetworkResult.Error -> {
                    _recipesListState.postValue(
                        RecipesListState(
                            categoryName = categoryName,
                            categoryImageUrl = fullImageUrl,
                            recipes = cachedRecipes.takeIf { it.isNotEmpty() }?.map {
                                it.copy(imageUrl = "$recipesImageUrl${it.imageUrl}")
                            } ?: emptyList(),
                            errorId = if (cachedRecipes.isEmpty()) result.errorId else null
                        )
                    )
                }
            }
        }
    }
}