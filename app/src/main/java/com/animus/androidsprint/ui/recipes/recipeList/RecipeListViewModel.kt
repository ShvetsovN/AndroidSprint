package com.animus.androidsprint.ui.recipes.recipeList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.animus.androidsprint.data.RecipeRepository
import com.animus.androidsprint.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RecipeRepository(application)

    private val _recipeListLiveData = MutableLiveData<RecipeListState>()
    val recipeListLiveData: LiveData<RecipeListState> = _recipeListLiveData

    fun loadRecipe(categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val recipe = repository.getRecipesByIds(categoryId)
            Log.i("RecipeListViewModel", "Loading recipes for categoryId: $categoryId")
            _recipeListLiveData.postValue(recipe?.let {
                RecipeListState(recipeList = it, isError = false)
            } ?: RecipeListState(isError = true))
        }
    }

    override fun onCleared() {
        Log.e("RecipeList", "VM cleared")
        super.onCleared()
    }

    data class RecipeListState(
        val recipeList: List<Recipe> = emptyList(),
        val isError: Boolean = false,
    )
}