package com.example.resipesdishesapp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.resipesdishesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
   private  var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding is null!")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
_binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}