package com.example.resipesdishesapp.data

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.resipesdishesapp.R
import com.example.resipesdishesapp.model.Category
import com.example.resipesdishesapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import java.util.concurrent.Executors

class RecipesRepository(private val context: Context) {

    private val threadPool = Executors.newFixedThreadPool(10)

    private val retrofit by lazy {
        val contentType = "application/json".toMediaType()
        Retrofit.Builder()
            .baseUrl("https://recipes.androidsprint.ru/api/")
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }

    fun getCategories(callback: (List<Category>) -> Unit) {
        threadPool.execute {
            try {
                val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)
                val categoriesResponse = service.getCategories().execute()
                if (categoriesResponse.isSuccessful) {
                    categoriesResponse.body()?.let {
                        callback(it)
                    } ?: run {
                        showError(R.string.errorData.toString())
                        callback(emptyList())
                    }
                }
            } catch (e: Exception) {
                showError(R.string.errorConnect.toString())
                callback(emptyList())
            }
        }
    }

    fun getRecipesCategoryId(categoryId: Int, callback: (List<Recipe>) -> Unit) {
        threadPool.execute {
            try {
                val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)
                val recipesResponse = service.getRecipesByCategoryId(categoryId).execute()
                if (recipesResponse.isSuccessful) {
                    recipesResponse.body()?.let {
                        callback(it)
                    } ?: run {
                        showError(R.string.errorData.toString())
                        callback(emptyList())
                    }
                }
            } catch (e: Exception) {
                showError(R.string.errorConnect.toString())
                callback(emptyList())
            }
        }
    }

    fun getRecipeById(recipeId: Int, callback: (Recipe) -> Unit) {
        threadPool.execute {
            try {
                val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)
                val recipeResponse = service.getRecipeById(recipeId).execute()
                if (recipeResponse.isSuccessful) {
                    recipeResponse.body()?.let {
                        callback(it)
                    } ?: run {
                        showError(R.string.errorData.toString())
                    }
                }
            } catch (e: Exception) {
                showError(R.string.errorConnect.toString())
            }
        }
    }

    fun getListRecipeId(recipesId: Set<Int>, callback: (List<Recipe>) -> Unit) {
        threadPool.execute {
            try {
                val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)
                val idsString = recipesId.joinToString(",")
                val recipesResponse = service.getListRecipeId(idsString).execute()
                if (recipesResponse.isSuccessful) {
                    recipesResponse.body()?.let {
                        callback(it)
                    } ?: run {
                        showError(R.string.errorData.toString())
                    }
                }
            } catch (e: Exception) {
                showError(R.string.errorConnect.toString())
            }
        }
    }

    private fun showError(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}