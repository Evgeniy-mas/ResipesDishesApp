package com.example.resipesdishesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.resipesdishesapp.databinding.FragmentResipeBinding


class RecipeFragment : Fragment() {

    private var _recipeBinding: FragmentResipeBinding? = null
    private val recipeBinding: FragmentResipeBinding
        get() = _recipeBinding
            ?: throw IllegalStateException("RecipeFragment must not be null")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _recipeBinding = FragmentResipeBinding.inflate(inflater, container, false)
        return recipeBinding.root
    }
}






