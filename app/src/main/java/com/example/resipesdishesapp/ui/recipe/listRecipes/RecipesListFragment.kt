package com.example.resipesdishesapp.ui.recipe.listRecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.resipesdishesapp.R
import com.example.resipesdishesapp.databinding.FragmentRecipesListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesListFragment : Fragment() {


    val viewModel: RecipesListViewModel by viewModels()
    private var _recipesBinding: FragmentRecipesListBinding? = null
    private val recipesBinding: FragmentRecipesListBinding
        get() = _recipesBinding
            ?: throw IllegalStateException("RecipesListFragment must not be null")
    private lateinit var recipesAdapter: RecipesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _recipesBinding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return recipesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryId = arguments?.getInt("categoryId") ?: return
        val categoryName = arguments?.getString("categoryName")
        val categoryImageUrl = arguments?.getString("categoryImageUrl")

        initRecycler()
        viewModel.loadData(categoryId, categoryName, categoryImageUrl)
    }

    private fun initRecycler() {
        recipesAdapter = RecipesListAdapter(emptyList()).apply {
            setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            })
        }
        recipesBinding.rvRecipes.adapter = recipesAdapter

        viewModel.recipesListState.observe(viewLifecycleOwner) { state ->
            state.categoryName?.let { recipesBinding.tvTitle.text = it }

            state.categoryImageUrl?.let { imageUrl ->
                Glide.with(requireContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_error)
                    .into(recipesBinding.ivCategoryImage)
            }

            recipesAdapter.updateRecipes(state.recipes)

            state.errorId?.let { resId ->
                Toast.makeText(requireContext(), resId, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {

        val direction =
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeId)

        findNavController().navigate(direction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _recipesBinding = null
    }
}