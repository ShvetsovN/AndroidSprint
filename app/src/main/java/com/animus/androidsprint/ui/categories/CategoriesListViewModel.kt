package com.animus.androidsprint.ui.categories

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.animus.androidsprint.data.RecipeRepository
import com.animus.androidsprint.model.Category
import java.util.concurrent.Executors

class CategoriesListViewModel : ViewModel() {

    private val _categoriesListLiveData = MutableLiveData<CategoriesListState>()
    val categoriesListLiveData: LiveData<CategoriesListState> = _categoriesListLiveData

    private val repository = RecipeRepository()
    private val threadPool = Executors.newFixedThreadPool(2)

    init {
        Log.e("CategoriesListVM", "VM created")
        loadCategories()
    }

    private fun loadCategories() {
        threadPool.execute {
            val categories = repository.getCategories()
            if (categories == null) {
                Toast.makeText(context, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
            } else {
                _categoriesListLiveData.value = CategoriesListState(categories = categories)
            }
        }
    }

    data class CategoriesListState(
        val categories: List<Category> = emptyList()
    )
}