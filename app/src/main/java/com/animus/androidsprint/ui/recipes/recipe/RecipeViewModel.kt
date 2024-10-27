package com.animus.androidsprint.ui.recipes.recipe

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.animus.androidsprint.Constants
import com.animus.androidsprint.data.RecipeRepository
import com.animus.androidsprint.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RecipeRepository(application)
    private val _recipeLiveData = MutableLiveData<RecipeState>()
    val recipeLiveData: LiveData<RecipeState> = _recipeLiveData

    override fun onCleared() {
        Log.e("RecipeVM", "cleared")
        super.onCleared()
    }

    fun updatingPortionCount(portionCount: Int) {
        _recipeLiveData.value = _recipeLiveData.value?.copy(portionCount = portionCount)
    }

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val recipe = repository.getRecipeById(recipeId)
            val favoriteRecipeIds = repository.getFavoriteRecipes()?.map { it.id }
            val isFavorite = favoriteRecipeIds?.contains(recipeId)
            val imageUrl = Constants.IMAGE_URL + recipe?.imageUrl

            _recipeLiveData.postValue(
                isFavorite?.let {
                    RecipeState(
                        recipe = recipe,
                        portionCount = recipeLiveData.value?.portionCount ?: 1,
                        isFavorite = it,
                        recipeImage = imageUrl
                    )
                }
            )
        }
    }

    fun onFavoritesClicked() {
        val recipeId = recipeLiveData.value?.recipe?.id ?: return
        val isFavorite = recipeLiveData.value?.isFavorite?.not() ?: return

        viewModelScope.launch {
            repository.updateFavoriteStatus(recipeId, isFavorite)
            _recipeLiveData.value = recipeLiveData.value?.copy(isFavorite = isFavorite)
        }
    }

    data class RecipeState(
        val recipe: Recipe? = null,
        val portionCount: Int = 1,
        val isFavorite: Boolean = false,
        val recipeImage: String?,
        val isError: Boolean = false,
    )
}
