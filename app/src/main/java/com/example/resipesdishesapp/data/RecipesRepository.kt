package com.example.resipesdishesapp.data



import com.example.resipesdishesapp.R
import com.example.resipesdishesapp.model.Category
import com.example.resipesdishesapp.model.Recipe
import com.example.resipesdishesapp.ui.category.CategoriesDao
import com.example.resipesdishesapp.ui.recipe.listRecipes.RecipesDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipesRepository(
    private val recipesDao: RecipesDao,
    private val categoriesDao: CategoriesDao,
    private val recipeApiService: RecipeApiService,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getCategories(): NetworkResult<List<Category>> {
        return withContext(ioDispatcher) {
            try {
                val categoriesResponse = recipeApiService.getCategories()
                NetworkResult.Success(categoriesResponse)
            } catch (e: Exception) {
                NetworkResult.Error(R.string.errorConnect)
            }
        }
    }

    suspend fun getRecipesCategoryId(categoryId: Int): NetworkResult<List<Recipe>> {
        return withContext(ioDispatcher) {
            try {
                val recipesResponse = recipeApiService.getRecipesByCategoryId(categoryId)
                NetworkResult.Success(recipesResponse)
            } catch (e: Exception) {
                NetworkResult.Error(R.string.errorConnect)
            }
        }
    }


    suspend fun getRecipeById(recipeId: Int): NetworkResult<Recipe> {
        return withContext(ioDispatcher) {
            try {
                val networkRecipe = recipeApiService.getRecipeById(recipeId)
                val isFavorite = recipesDao.isFavorite(recipeId)
                val combinedRecipe = networkRecipe.copy(isFavorite = isFavorite)
                recipesDao.insert(combinedRecipe)

                NetworkResult.Success(combinedRecipe)
            } catch (e: Exception) {

                val localRecipe = recipesDao.getById(recipeId)
                if (localRecipe != null) {
                    NetworkResult.Success(localRecipe)
                } else {
                    NetworkResult.Error(R.string.errorConnect)
                }
            }
        }
    }

    suspend fun isFavorite(recipeId: Int): Boolean = withContext(Dispatchers.IO) {
        recipesDao.isFavorite(recipeId)
    }

    suspend fun getCategoriesFromCache(): List<Category> {
        return categoriesDao.getAll()
    }

    suspend fun saveCategoriesToCache(categories: List<Category>) {
        categoriesDao.insertAll(categories)
    }

    suspend fun getRecipesFromCache(categoryId: Int): List<Recipe> {
        return recipesDao.getByCategoryId(categoryId)
    }

    suspend fun saveRecipesToCache(recipes: List<Recipe>) {
        recipesDao.insertAll(recipes)
    }

    suspend fun setFavorite(recipeId: Int, isFavorite: Boolean) {
        recipesDao.setFavorite(recipeId, isFavorite)
    }

    suspend fun getFavoriteRecipes(): List<Recipe> {
        return recipesDao.getFavorites()
    }
}