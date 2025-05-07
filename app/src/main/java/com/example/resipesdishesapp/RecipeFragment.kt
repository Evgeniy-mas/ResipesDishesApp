package com.example.resipesdishesapp

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.resipesdishesapp.databinding.FragmentResipeBinding


class RecipeFragment : Fragment() {


    companion object {
        const val ARG_RECIPE = "recipe"
    }

    private lateinit var recipe: Recipe


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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBundleData()
        initUI()
    }

    private fun initBundleData() {
        recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
        } else {

            arguments?.getParcelable(ARG_RECIPE)
        } ?: throw IllegalStateException("Recipe not found in arguments")
    }

    private fun initUI() {
        with(recipeBinding) {

            tvTitle.text = recipe.title
        }
    }
}






