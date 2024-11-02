package com.animus.androidsprint.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animus.androidsprint.Constants
import com.animus.androidsprint.data.RecipeRepository
import com.animus.androidsprint.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val recipeRepository: RecipeRepository,
) : ViewModel() {

    private val _recipeLiveData = MutableLiveData<RecipeState>()
    val recipeLiveData: LiveData<RecipeState> = _recipeLiveData

    override fun onCleared() {
        Log.e("RecipeVM", "cleared")
        super.onCleared()
    }

    fun updatingPortionCount(portionCount: Int) {
        _recipeLiveData.value = _recipeLiveData.value?.copy(portionCount = portionCount)
    }

    fun resetPortionCount(){
        updatingPortionCount(1)
    }

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            var recipe = recipeRepository.getRecipeByIdFromCache(recipeId)
            if (recipe == null) {
                recipe = recipeRepository.getRecipeFromServerById(recipeId)

                recipe?.let {
                    recipeRepository.saveRecipesToCache(listOf(it), it.categoryId)
                }
            }
            val favoriteRecipeIds = recipeRepository.getFavoriteRecipes().map { it.id }
            val isFavorite = favoriteRecipeIds.contains(recipeId)
            val imageUrl = Constants.IMAGE_URL + recipe?.imageUrl

            val currentPortionCount = _recipeLiveData.value?.portionCount ?: 1

            _recipeLiveData.postValue(
                RecipeState(
                    recipe = recipe,
                    portionCount = currentPortionCount,
                    isFavorite = isFavorite,
                    recipeImage = imageUrl
                )
            )
        }
    }

    fun onFavoritesClicked() {
        val recipeId = recipeLiveData.value?.recipe?.id ?: return
        val isFavorite = recipeLiveData.value?.isFavorite?.not() ?: return

        viewModelScope.launch {
            recipeRepository.updateFavoriteStatus(recipeId, isFavorite)
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
