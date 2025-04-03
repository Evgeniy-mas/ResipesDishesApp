package com.example.resipesdishesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.resipesdishesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.commit {
            replace<CategoriesListFragment>(R.id.mainContainer)
            setReorderingAllowed(true)

        }

        binding.btnFavorites.setOnClickListener {
            supportFragmentManager.commit {
                replace<FavoritesFragment>(R.id.mainContainer)
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }

        binding.btnCategory.setOnClickListener {
            supportFragmentManager.commit {
                replace<CategoriesListFragment>(R.id.mainContainer)
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }
    }
}