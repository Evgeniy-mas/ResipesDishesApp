package com.example.resipesdishesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.resipesdishesapp.databinding.FragmentFavoritesBinding


class FavoritesFragment : Fragment() {

    private var _favoritesBinding: FragmentFavoritesBinding? = null
    private val favoritesBinding: FragmentFavoritesBinding
        get() = _favoritesBinding ?: throw IllegalStateException("FragmentListCategoriesBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _favoritesBinding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return favoritesBinding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _favoritesBinding = null
    }
}


