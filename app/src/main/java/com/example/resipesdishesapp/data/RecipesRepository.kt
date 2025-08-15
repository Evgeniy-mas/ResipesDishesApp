package com.example.resipesdishesapp.data

import android.content.Context

import com.example.resipesdishesapp.R
import com.example.resipesdishesapp.model.Category
import com.example.resipesdishesapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class RecipesRepository(val context: Context) {

    private val db = AppDatabase.getDatabase(context.applicationContext)
    private val categoriesDao = db.categoriesDao()
    private val recipesDao = db.RecipesDao()

    private val retrofit by lazy {
        val contentType = "application/json".toMediaType()
        Retrofit.Builder()
            .baseUrl("https://recipes.androidsprint.ru/api/")
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }
    private val service by lazy {
        retrofit.create(RecipeApiService::class.java)
    }

    suspend fun getCategories(): NetworkResult<List<Category>> {
        return withContext(Dispatchers.IO) {
            try {
                val categoriesResponse = service.getCategories()
                NetworkResult.Success(categoriesResponse)
            } catch (e: Exception) {
                NetworkResult.Error(R.string.errorConnect)
            }
        }
    }

    suspend fun getRecipesCategoryId(categoryId: Int): NetworkResult<List<Recipe>> {
        return withContext(Dispatchers.IO) {
            try {
                val recipesResponse = service.getRecipesByCategoryId(categoryId)
                NetworkResult.Success(recipesResponse)
            } catch (e: Exception) {
                NetworkResult.Error(R.string.errorConnect)
            }
        }
    }


    suspend fun getRecipeById(recipeId: Int): NetworkResult<Recipe> {
        return withContext(Dispatchers.IO) {
            try {
                val networkRecipe = service.getRecipeById(recipeId)
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