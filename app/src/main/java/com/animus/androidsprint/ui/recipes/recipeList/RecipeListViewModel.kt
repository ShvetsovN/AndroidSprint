package com.animus.androidsprint.ui.recipes.recipeList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.animus.androidsprint.data.STUB
import com.animus.androidsprint.model.Recipe

class RecipeListViewModel : ViewModel() {

    private val _recipeListLiveData = MutableLiveData<RecipeListState>()
    val recipeListLiveData: LiveData<RecipeListState> = _recipeListLiveData

    fun loadRecipe(categoryId: Int) {
        val recipe = STUB.getRecipesByCategoryId(categoryId)
        _recipeListLiveData.value = RecipeListState(
            recipeList = recipe
        )
    }

    override fun onCleared() {
        Log.e("RecipeList", "VM cleared")
        super.onCleared()
    }

    init{
        Log.e("RecipeList", "VM created")
    }

    data class RecipeListState(
        val recipeList: List<Recipe> = emptyList()
    )

}