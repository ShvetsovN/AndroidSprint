package com.animus.androidsprint.ui.recipes.recipeList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animus.androidsprint.data.RecipeRepository
import com.animus.androidsprint.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeListViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    private val _recipeListLiveData = MutableLiveData<RecipeListState>()
    val recipeListLiveData: LiveData<RecipeListState> = _recipeListLiveData

    fun loadRecipe(categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _recipeListLiveData.value ?: RecipeListState()

            val cacheRecipe = recipeRepository.getRecipesFromCacheByCategoryId(categoryId)
            if (cacheRecipe.isNotEmpty()) {
                _recipeListLiveData.postValue(currentState.copy(recipeList = cacheRecipe))
            } else {
                val recipesFromServer = recipeRepository.getRecipesByIds(categoryId)

                if (recipesFromServer == null) {
                    _recipeListLiveData.postValue(RecipeListState(isError = true))
                } else {
                    val newState = currentState.copy(recipeList = recipesFromServer, isError = false)
                    _recipeListLiveData.postValue(newState)

                    recipeRepository.saveRecipesToCache(recipesFromServer, categoryId)
                }
            }
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