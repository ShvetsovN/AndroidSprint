package com.animus.androidsprint.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.animus.androidsprint.Constants
import com.animus.androidsprint.data.STUB
import com.animus.androidsprint.model.Recipe

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val _recipeLiveData = MutableLiveData<RecipeState>().apply {
        value = RecipeState()
    }
    val recipeLiveData: LiveData<RecipeState> = _recipeLiveData

    init {
        Log.i("!!!", "VM created")
    }

    fun loadRecipe(recipeId: Int) {
        //TODO ("load from network")
        val favorites = getFavorites().contains(recipeId.toString())
        val recipe = STUB.getRecipeById(recipeId)
        _recipeLiveData.value = RecipeState(
            recipe = recipe,
            portionCount = _recipeLiveData.value?.portionCount ?: 1,
            isFavorite = favorites,
        )
    }

    fun onFavoritesClicked() {
        val currentState = _recipeLiveData.value ?: return
        val recipe = currentState.recipe ?: return
        val recipeId = recipe.id.toString()

        val newIsFavorite = !currentState.isFavorite

        _recipeLiveData.value = currentState.copy(isFavorite = newIsFavorite)

        val favorites = getFavorites().toMutableSet()
        if (newIsFavorite) {
            favorites.add(recipeId)
        } else {
            favorites.remove(recipeId)
        }
        saveFavorites(favorites)
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

    private fun saveFavorites(favoriteRecipeId: Set<String>) {
        val sharedPrefs =
            getApplication<Application>().getSharedPreferences(
                Constants.PREFERENCE_RECIPE_NAME,
                Context.MODE_PRIVATE
            )
                ?: return
        with(sharedPrefs.edit()) {
            putStringSet(
                Constants.PREFERENCE_RECIPE_FAVORITES,
                favoriteRecipeId
            )
            apply()
        }
    }

    override fun onCleared() {
        Log.i("!!!", "VM cleared")
        super.onCleared()
    }

    data class RecipeState(
        val recipe: Recipe? = null,
        val portionCount: Int = 1,
        val isFavorite: Boolean = false,
    )
}
