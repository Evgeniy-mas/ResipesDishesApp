package com.example.resipesdishesapp.ui.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.resipesdishesapp.data.STUB
import com.example.resipesdishesapp.model.Category

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {

    private val _categoriesState = MutableLiveData(CategoriesListState())
    val categoriesState: LiveData<CategoriesListState> get() = _categoriesState

    data class CategoriesListState(
        val categories: List<Category> = emptyList()
    )

    fun loadCategories() {
        val categories = STUB.getCategories()
        _categoriesState.value = CategoriesListState(categories = categories)
    }
}