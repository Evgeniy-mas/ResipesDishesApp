package com.example.resipesdishesapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.resipesdishesapp.databinding.FragmentRecipesListBinding


class RecipesListFragment : Fragment() {

    companion object {
        const val ARG_CATEGORY_ID = "category_id"
        const val ARG_CATEGORY_NAME = "category_name"
        const val ARG_CATEGORY_IMAGE_URL = "category_image_url"
    }

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null


    private var _recipesBinding: FragmentRecipesListBinding? = null
    private val recipesBinding: FragmentRecipesListBinding
        get() = _recipesBinding
            ?: throw IllegalStateException("RecipesListFragment must not be null")


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
        initBundleData()
        initRecycler()


    }

    private fun initBundleData() {
        arguments?.let { bundle ->
            categoryId = bundle.getInt(ARG_CATEGORY_ID)
            categoryName = bundle.getString(ARG_CATEGORY_NAME)
            categoryImageUrl = bundle.getString(ARG_CATEGORY_IMAGE_URL)

            _recipesBinding?.tvTitle?.text = categoryName

            val drawable =
                try {
                    Drawable.createFromStream(
                        categoryImageUrl?.let { requireContext().assets.open(it) },
                        null
                    )
                } catch (e: Exception) {
                    val errorMessage = requireContext().getString(
                        R.string.drawable_error
                    )
                    Log.e("!!!", "$errorMessage $categoryImageUrl", e)
                    null
                }
            _recipesBinding?.ivCategoryImage?.setImageDrawable(drawable)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _recipesBinding = null
    }

    private fun initRecycler() {
        val recipesAdapter = RecipesListAdapter(STUB.getRecipesByCategoryId(categoryId))
        recipesBinding.rvRecipes.adapter = recipesAdapter

        recipesAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }

        }
        )
    }

    fun openRecipeByRecipeId(recipeId: Int) {

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<ResipeFragment>(R.id.mainContainer)
            addToBackStack(null)
        }
    }
}




