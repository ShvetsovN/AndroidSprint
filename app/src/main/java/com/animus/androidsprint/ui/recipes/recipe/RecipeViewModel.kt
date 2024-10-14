package com.animus.androidsprint.ui.recipes.recipe

import android.app.Application
import android.content.Context
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
            val favorites = getFavorites().contains(recipeId.toString())
            val imageUrl = Constants.IMAGE_URL + recipe?.imageUrl

            _recipeLiveData.postValue(
                RecipeState(
                    recipe = recipe,
                    portionCount = recipeLiveData.value?.portionCount ?: 1,
                    isFavorite = favorites,
                    recipeImage = imageUrl
                )
            )
        }
    }

    fun onFavoritesClicked() {
        val favorites = getFavorites()
        val recipeId = recipeLiveData.value?.recipe?.id.toString()
        if (recipeId.isNotEmpty()) {
            var isFavorite = favorites.contains(recipeId)
            isFavorite = !isFavorite

            if (isFavorite) {
                favorites.add(recipeId)
            } else {
                favorites.remove(recipeId)
            }

            saveFavorites(favorites)

            _recipeLiveData.value = recipeLiveData.value?.copy(isFavorite = isFavorite)
        }
    }

    private fun saveFavorites(favoriteRecipeId: Set<String>) {
        val sharedPrefs =
            getApplication<Application>().getSharedPreferences(
                Constants.PREFERENCE_RECIPE_NAME,
                Context.MODE_PRIVATE
            )
                ?: return
        with(sharedPrefs.edit()) {
            putStringSet(Constants.PREFERENCE_RECIPE_FAVORITES, favoriteRecipeId)
            apply()
        }
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs =
            getApplication<Application>().getSharedPreferences(
                Constants.PREFERENCE_RECIPE_NAME,
                Context.MODE_PRIVATE
            )
        val savedSet =
            sharedPrefs?.getStringSet(Constants.PREFERENCE_RECIPE_FAVORITES, mutableSetOf())
                ?: mutableSetOf()
        return HashSet(savedSet)
    }

    data class RecipeState(
        val recipe: Recipe? = null,
        val portionCount: Int = 1,
        val isFavorite: Boolean = false,
        val recipeImage: String?,
        val isError: Boolean = false,
    )
}
