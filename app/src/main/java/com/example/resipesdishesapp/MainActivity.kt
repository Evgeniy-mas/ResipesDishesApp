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
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        Log.i(
            "onCreateThread",
            "Метод onCreate() выполняется на потоке:${Thread.currentThread().name}"
        )

        val thread = Thread {
            Log.i("thread", "Выполняю запрос на потоке: ${Thread.currentThread().name}")

            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            val responseBody = connection.inputStream.bufferedReader().readText()
            val categories = Json.decodeFromString<List<Category>>(responseBody)
            Log.i("data", "$categories")

            Log.i("!!!", "body: $responseBody")
        }
        thread.start()

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
}