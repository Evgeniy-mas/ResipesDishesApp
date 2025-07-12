package com.example.resipesdishesapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.example.resipesdishesapp.databinding.ActivityMainBinding
import com.example.resipesdishesapp.model.Category
import com.example.resipesdishesapp.model.Recipe
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val threadPool = Executors.newFixedThreadPool(10)

    private val loggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(loggingInterceptor)
        .build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        threadPool.execute {
            Log.i("Network", "Запрос категорий на потоке: ${Thread.currentThread().name}")
            val categoryRequest = Request.Builder()
                .url("https://recipes.androidsprint.ru/api/category")
                .build()

            okHttpClient.newCall(categoryRequest).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.e("Error", "Ошибка запроса категорий: ${response.code}")
                }
                val responseBody = response.body?.string() ?: ""
                val categories = Json.decodeFromString<List<Category>>(responseBody)
                Log.i("Categories", "Всего ${categories.size} категорий")

                categories.forEach { category ->
                    threadPool.execute {
                        Log.i(
                            "Network",
                            "Запрос рецептов для ${category.title} на потоке: ${Thread.currentThread().name}"
                        )
                        val recipesRequest = Request.Builder()
                            .url("https://recipes.androidsprint.ru/api/category/${category.id}/recipes")
                            .build()

                        okHttpClient.newCall(recipesRequest).execute().use { recipesResponse ->
                            if (!recipesResponse.isSuccessful) {
                                Log.e("Error", "Ошибка запроса рецептов: ${recipesResponse.code}")
                            }

                            val responseBodyRecipe = recipesResponse.body?.string() ?: ""
                            val recipes = Json.decodeFromString<List<Recipe>>(responseBodyRecipe)
                            Log.i(
                                "!!!",
                                "Для категории ${category.title} получено ${recipes.size} рецептов"
                            )
                            Log.i("AllRecipes", "$recipes")
                        }
                    }
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnFavorites.setOnClickListener {
            val currentFragment = findNavController(R.id.mainContainer).currentDestination?.id

            when (currentFragment) {
                R.id.categoriesListFragment -> {
                    findNavController(R.id.mainContainer).navigate(
                        R.id.action_categoriesListFragment_to_favoritesFragment
                    )
                }

                R.id.recipesListFragment -> {
                    findNavController(R.id.mainContainer).navigate(
                        R.id.action_recipesListFragment_to_favoritesFragment
                    )
                }

                R.id.recipeFragment -> {
                    findNavController(R.id.mainContainer).navigate(
                        R.id.action_recipeFragment_to_favoritesFragment
                    )
                }

                else -> {
                    findNavController(R.id.mainContainer).navigate(R.id.favoritesFragment)
                }
            }
        }

        binding.btnCategory.setOnClickListener {
            val currentFragment = findNavController(R.id.mainContainer).currentDestination?.id

            when (currentFragment) {
                R.id.recipesListFragment -> {
                    findNavController(R.id.mainContainer).navigate(
                        R.id.action_recipesListFragment_to_categoriesListFragment
                    )
                }

                R.id.recipeFragment -> {
                    findNavController(R.id.mainContainer).navigate(
                        R.id.action_recipeFragment_to_categoriesListFragment
                    )
                }

                R.id.favoritesFragment -> {
                    findNavController(R.id.mainContainer).navigate(
                        R.id.action_favoritesFragment_to_categoriesListFragment
                    )
                }

                else -> {
                    findNavController(R.id.mainContainer).navigate(R.id.categoriesListFragment)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        threadPool.shutdown()
    }
}