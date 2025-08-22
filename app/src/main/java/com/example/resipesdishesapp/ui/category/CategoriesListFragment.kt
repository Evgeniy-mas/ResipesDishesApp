package com.example.resipesdishesapp.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.resipesdishesapp.databinding.FragmentListCategoriesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesListFragment : Fragment() {
    private var _categoriesBinding: FragmentListCategoriesBinding? = null
    private val categoriesBinding: FragmentListCategoriesBinding
        get() = _categoriesBinding
            ?: throw IllegalStateException("FragmentListCategoriesBinding must not be null")

    private val viewModel: CategoriesListViewModel by viewModels()
    private lateinit var categoriesAdapter: CategoriesListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _categoriesBinding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return categoriesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        viewModel.loadCategories()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _categoriesBinding = null
    }

    private fun initRecycler() {

        categoriesAdapter = CategoriesListAdapter(emptyList()).apply {
            setOnItemClickListener(object : CategoriesListAdapter.OnItemClickListener {
                override fun onItemClick(categoryId: Int) {
                    openRecipesByCategoryId(categoryId)
                }
            })
        }
        categoriesBinding.rvCategories.adapter = categoriesAdapter

        viewModel.categoriesState.observe(viewLifecycleOwner) { state ->
            categoriesAdapter.updateCategories(state.categories, state.imageUrl)

            state.errorId?.let { resId ->
                Toast.makeText(requireContext(), resId, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun openRecipesByCategoryId(categoryId: Int) {
        val category = viewModel.categoriesState.value?.categories?.find { it.id == categoryId }
            ?: throw IllegalArgumentException("Category not found")

        val direction = CategoriesListFragmentDirections
            .actionCategoriesListFragmentToRecipesListFragment(
                categoryId = categoryId,
                categoryName = category.title,
                categoryImageUrl = category.imageUrl
            )
        findNavController().navigate(direction)
    }
}