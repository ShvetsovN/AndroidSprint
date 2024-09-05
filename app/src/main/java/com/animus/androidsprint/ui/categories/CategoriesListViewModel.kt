package com.animus.androidsprint.ui.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.animus.androidsprint.data.STUB
import com.animus.androidsprint.model.Category

class CategoriesListViewModel : ViewModel() {

    private val _categoriesListLiveData = MutableLiveData<CategoriesListState>()
    val categoriesListLiveData: LiveData<CategoriesListState> = _categoriesListLiveData

    init {
        Log.e("CategoriesListVM", "VM created")
        loadCategories()
    }

    private fun loadCategories() {
        val categories: List<Category> = STUB.getCategories()
        _categoriesListLiveData.value = CategoriesListState(
            categories = categories
        )
    }

    fun getCategoryById(categoryId: Int): Category? {
        return _categoriesListLiveData.value?.categories?.find { it.id == categoryId }
    }

    data class CategoriesListState(
        val categories: List<Category> = emptyList()
    )
}