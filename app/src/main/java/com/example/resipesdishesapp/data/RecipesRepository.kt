package com.example.resipesdishesapp.data

import com.example.resipesdishesapp.R
import com.example.resipesdishesapp.model.Category
import com.example.resipesdishesapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import java.util.concurrent.Executors

class RecipesRepository {

    private val threadPool = Executors.newFixedThreadPool(10)

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

    fun getCategories(callback: (NetworkResult<List<Category>>) -> Unit) {
        threadPool.execute {
            try {
                val categoriesResponse = service.getCategories().execute()
                if (categoriesResponse.isSuccessful) {
                    categoriesResponse.body()?.let {
                        callback(NetworkResult.Success(it))
                    } ?: run {
                        callback(NetworkResult.Error(R.string.errorData))
                    }
                }
            } catch (e: Exception) {
                callback(NetworkResult.Error(R.string.errorConnect))
            }
        }
    }

    fun getRecipesCategoryId(categoryId: Int, callback: (NetworkResult<List<Recipe>>) -> Unit) {
        threadPool.execute {
            try {
                val recipesResponse = service.getRecipesByCategoryId(categoryId).execute()
                if (recipesResponse.isSuccessful) {
                    recipesResponse.body()?.let {
                        callback(NetworkResult.Success(it))
                    } ?: run {
                        callback(NetworkResult.Error(R.string.errorData))
                    }
                }
            } catch (e: Exception) {
                callback(NetworkResult.Error(R.string.errorConnect))
            }
        }
    }

    fun getRecipeById(recipeId: Int, callback: (NetworkResult<Recipe>) -> Unit) {
        threadPool.execute {
            try {

                val recipeResponse = service.getRecipeById(recipeId).execute()
                if (recipeResponse.isSuccessful) {
                    recipeResponse.body()?.let {
                        callback(NetworkResult.Success(it))
                    } ?: run {
                        callback(NetworkResult.Error(R.string.errorData))
                    }
                }
            } catch (e: Exception) {
                callback(NetworkResult.Error(R.string.errorConnect))
            }
        }
    }

    fun getListRecipeId(recipesId: Set<Int>, callback: (NetworkResult<List<Recipe>>) -> Unit) {
        threadPool.execute {
            try {
                val idsString = recipesId.joinToString(",")
                val recipesResponse = service.getListRecipeId(idsString).execute()
                if (recipesResponse.isSuccessful) {
                    recipesResponse.body()?.let {
                        callback(NetworkResult.Success(it))
                    } ?: run {
                        callback(NetworkResult.Error(R.string.errorData))
                    }
                }
            } catch (e: Exception) {
                callback(NetworkResult.Error(R.string.errorConnect))
            }
        }
    }
}