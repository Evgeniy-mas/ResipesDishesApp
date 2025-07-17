package com.example.resipesdishesapp.data

import com.example.resipesdishesapp.model.Category
import com.example.resipesdishesapp.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {
    @GET("category")
    fun getCategories(): Call<List<Category>>

    @GET("category/{categoryId}/recipes")
    fun getRecipesByCategoryId(@Path("categoryId") categoryId: Int): Call<List<Recipe>>

    @GET("recipe/{recipeId}")
    fun getRecipeById(@Path("recipeId") recipeId: Int): Call<Recipe>

    @GET("recipes")
    fun getListRecipeId(@Query("ids") recipeIds: String): Call<List<Recipe>>
}


