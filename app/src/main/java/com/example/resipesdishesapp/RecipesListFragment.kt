package com.example.resipesdishesapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.resipesdishesapp.databinding.FragmentRecipesListBinding


class RecipesListFragment : Fragment() {

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



        arguments?.let { bundle ->
            categoryId = bundle.getInt("ARG_CATEGORY_ID")
            categoryName = bundle.getString("ARG_CATEGORY_NAME")
            categoryImageUrl = bundle.getString("ARG_CATEGORY_IMAGE_URL")

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
}




