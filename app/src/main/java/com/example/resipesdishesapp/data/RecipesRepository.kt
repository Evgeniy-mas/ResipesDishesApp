package com.example.resipesdishesapp.data

import com.example.resipesdishesapp.R
import com.example.resipesdishesapp.model.Category
import com.example.resipesdishesapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class RecipesRepository {

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
                val recipeResponse = service.getRecipeById(recipeId)
                NetworkResult.Success(recipeResponse)
            } catch (e: Exception) {
                NetworkResult.Error(R.string.errorConnect)
            }
        }
    }

    suspend fun getListRecipeId(recipesId: Set<Int>): NetworkResult<List<Recipe>> {
        return withContext(Dispatchers.IO) {
            try {
                val idsString = recipesId.joinToString(",")
                val recipesResponse = service.getListRecipeId(idsString)
                NetworkResult.Success(recipesResponse)
            } catch (e: Exception) {
                NetworkResult.Error(R.string.errorConnect)
            }
        }
    }
}