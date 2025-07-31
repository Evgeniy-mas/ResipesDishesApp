package com.example.resipesdishesapp.data

import com.example.resipesdishesapp.model.Category
import com.example.resipesdishesapp.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {
    @GET("category")
    suspend fun getCategories(): List<Category>

    @GET("category/{categoryId}/recipes")
    suspend fun getRecipesByCategoryId(@Path("categoryId") categoryId: Int): List<Recipe>

    @GET("recipe/{recipeId}")
    suspend fun getRecipeById(@Path("recipeId") recipeId: Int): Recipe

    @GET("recipes")
    suspend fun getListRecipeId(@Query("ids") recipeIds: String): List<Recipe>
}


