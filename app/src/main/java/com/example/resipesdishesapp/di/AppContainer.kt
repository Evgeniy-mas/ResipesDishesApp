package com.example.resipesdishesapp.di

import android.content.Context
import com.example.resipesdishesapp.data.AppDatabase
import com.example.resipesdishesapp.data.RecipeApiService
import com.example.resipesdishesapp.data.RecipesRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class AppContainer(context: Context) {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val db = AppDatabase.getDatabase(context)
    private val categoriesDao = db.categoriesDao()
    private val recipesDao = db.RecipesDao()

    private val contentType = "application/json".toMediaType()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)
     val repository = RecipesRepository(
        recipesDao = recipesDao,
        categoriesDao = categoriesDao,
        recipeApiService = service,
        ioDispatcher = ioDispatcher
    )

    val categoriesListViewModelFactory = CategoriesListViewModelFactory(repository)
    val recipesListViewModelFactory = RecipesListViewModelFactory(repository)
    val recipeViewModelFactory = RecipeViewModelFactory(repository)

    val favoritesViewModelFactory = FavoritesViewModelFactory(repository)
}