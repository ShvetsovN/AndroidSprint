package com.animus.androidsprint.ui.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animus.androidsprint.data.RecipeRepository
import com.animus.androidsprint.model.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriesListViewModel : ViewModel() {

    private val _categoriesListLiveData = MutableLiveData<CategoriesListState>()
    val categoriesListLiveData: LiveData<CategoriesListState> = _categoriesListLiveData

    private val repository = RecipeRepository()

    init {
        Log.e("CategoriesListVM", "VM created")
    }

    fun loadCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _categoriesListLiveData.value ?: CategoriesListState()
            val categories = repository.getCategories()

            if (categories == null) {
                _categoriesListLiveData.postValue(CategoriesListState(isError = true))
            } else {
                val newState = currentState.copy(categories = categories, isError = false)
                _categoriesListLiveData.postValue(newState)
            }
        }
    }

    data class CategoriesListState(
        val categories: List<Category> = emptyList(),
        val isError: Boolean = false
    )
}