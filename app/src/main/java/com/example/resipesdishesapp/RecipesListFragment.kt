package com.example.resipesdishesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.resipesdishesapp.databinding.FragmentRecipesListBinding


class RecipesListFragment : Fragment() {

    private var _recipesBinding: FragmentRecipesListBinding? = null
    private val recipesBinding: FragmentRecipesListBinding
        get() = _recipesBinding ?: throw IllegalStateException("RecipesListFragment must not be null")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _recipesBinding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return recipesBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _recipesBinding = null
    }
}




