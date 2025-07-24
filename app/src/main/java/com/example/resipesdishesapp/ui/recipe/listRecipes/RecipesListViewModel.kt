package com.example.resipesdishesapp.ui.recipe.listRecipes

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.resipesdishesapp.R
import com.example.resipesdishesapp.data.NetworkResult
import com.example.resipesdishesapp.data.RecipesRepository
import com.example.resipesdishesapp.model.Recipe

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    private val _recipesListState = MutableLiveData(RecipesListState())
    val recipesListState: LiveData<RecipesListState> get() = _recipesListState
    private val recipesRepository = RecipesRepository()

    data class RecipesListState(
        val categoryName: String? = null,
        val categoryImage: Drawable? = null,
        val recipes: List<Recipe> = emptyList(),
        val errorId: Int? = null
    )

    fun loadData(categoryId: Int, categoryName: String?, categoryImageUrl: String?) {
        val drawable =
            try {
                Drawable.createFromStream(
                    categoryImageUrl?.let { getApplication<Application>().assets.open(it) },
                    null
                )
            } catch (e: Exception) {
                val errorMessage = getApplication<Application>().getString(
                    R.string.drawable_error
                )
                Log.e("!!!", "$errorMessage $categoryImageUrl", e)
                null
            }

        _recipesListState.value = RecipesListState(
            categoryName = categoryName,
            categoryImage = drawable,
            recipes = emptyList()
        )

        recipesRepository.getRecipesCategoryId(categoryId) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    _recipesListState.postValue(
                        RecipesListState(
                            categoryName = categoryName,
                            categoryImage = drawable,
                            recipes = result.data
                        )
                    )
                }

                is NetworkResult.Error -> {
                    _recipesListState.postValue(
                        RecipesListState(
                            categoryName = categoryName,
                            categoryImage = drawable,
                            recipes = emptyList(),
                            errorId = result.errorId
                        )
                    )
                }
            }
        }
    }
}