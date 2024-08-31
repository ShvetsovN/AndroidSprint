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
        Log.e("!!!", "Categories VM created")
    }

    fun loadCategories() {
        val category = STUB.getCategories()
        _categoriesListLiveData.value = CategoriesListState(
            category = category
        )
    }

    data class CategoriesListState(
        val category: List<Category> = emptyList()
    )
}