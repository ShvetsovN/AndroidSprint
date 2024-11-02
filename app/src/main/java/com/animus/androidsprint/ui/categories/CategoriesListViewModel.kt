package com.animus.androidsprint.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animus.androidsprint.data.RecipeRepository
import com.animus.androidsprint.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _categoriesListLiveData = MutableLiveData<CategoriesListState>()
    val categoriesListLiveData: LiveData<CategoriesListState> = _categoriesListLiveData

    fun loadCategories() {
        viewModelScope.launch {
            val currentState = _categoriesListLiveData.value ?: CategoriesListState()

            val cacheCategories: List<Category> = recipeRepository.getCategoriesFromCache()
            if (cacheCategories.isNotEmpty()) {
                _categoriesListLiveData.postValue(currentState.copy(categories = cacheCategories))
            } else {
                val categoriesFromServer: List<Category>? = recipeRepository.getCategories()

                if (categoriesFromServer == null) {
                    _categoriesListLiveData.postValue(CategoriesListState(isError = true))
                } else {
                    val newState =
                        currentState.copy(categories = categoriesFromServer, isError = false)
                    _categoriesListLiveData.postValue(newState)
                    recipeRepository.saveCategoriesToCache(categoriesFromServer)
                }
            }
        }
    }

    data class CategoriesListState(
        val categories: List<Category> = emptyList(),
        val isError: Boolean = false
    )
}