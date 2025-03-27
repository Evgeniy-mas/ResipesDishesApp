package com.example.resipesdishesapp
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.resipesdishesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val fragmentCategories:Fragment = CategoriesListFragment()

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()


        transaction.replace(R.id.main_Container,fragmentCategories)
            .commit()
    }
}