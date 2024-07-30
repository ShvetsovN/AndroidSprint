package com.animus.androidsprint.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.animus.androidsprint.Constants
import com.animus.androidsprint.R
import com.animus.androidsprint.data.STUB
import com.animus.androidsprint.model.Recipe

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val _recipeLiveData = MutableLiveData<RecipeState>()
    val recipeLiveData: LiveData<RecipeState> = _recipeLiveData

    init {
        Log.i("!!!", "VM created")

        _recipeLiveData.value = RecipeState()
    }

    override fun onCleared() {
        Log.i("!!!", "VM cleared")
        super.onCleared()
    }

    fun loadRecipe(recipeId: Int) {
        //TODO ("load from network")
        val favorites = getFavorites().contains(recipeId.toString())
        val recipe = STUB.getRecipeById(recipeId)
        _recipeLiveData.value = RecipeState(
            recipe = recipe,
            portionCount = recipeLiveData.value?.portionCount ?: 1,
            isFavorite = favorites
        )
    }

    fun onFavoritesClicked() {
        val favorites = getFavorites()
        val recipeId = recipeLiveData.value?.recipe?.id.toString()
        var isFavorite = favorites.contains(recipeId)
        isFavorite = !isFavorite
        if (isFavorite) {
            favorites.add(recipeId)
        } else {
            favorites.remove(recipeId)
        }
        saveFavorites(favorites)
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
    )
}
