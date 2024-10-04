package com.animus.androidsprint.ui.recipes.recipeList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.animus.androidsprint.data.RecipeRepository
import com.animus.androidsprint.model.Recipe
import java.util.concurrent.Executors

class RecipeListViewModel : ViewModel() {

    private val repository = RecipeRepository()
    private val threadPool = Executors.newFixedThreadPool(2)

    private val _recipeListLiveData = MutableLiveData<RecipeListState>()
    val recipeListLiveData: LiveData<RecipeListState> = _recipeListLiveData

    fun loadRecipe(categoryId: Int) {
        threadPool.execute {
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