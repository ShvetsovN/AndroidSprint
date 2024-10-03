package com.animus.androidsprint.ui.recipes.favorites

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.animus.androidsprint.Constants
import com.animus.androidsprint.data.RecipeRepository
import com.animus.androidsprint.model.Recipe
import java.util.concurrent.Executors

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RecipeRepository()
    private val threadPool = Executors.newFixedThreadPool(2)
    private val _favoriteLiveData = MutableLiveData<FavoriteState>()
    val favoriteLiveData: LiveData<FavoriteState> = _favoriteLiveData

    init {
        Log.e("FavoriteVM", "VM favorite created")
    }

    fun loadFavorites() {
        threadPool.execute {
            val currentState = _favoriteLiveData.value ?: FavoriteState()
            val favoritesRecipe: List<Recipe>? =
                repository.getRecipesByIds(getFavorites().map { it.toInt() }.toSet())
            Log.e("FavoriteVM", "Loaded favorite recipes: $favoritesRecipe")

            val newState = if (favoritesRecipe != null) {
                currentState.copy(recipeList = favoritesRecipe)
            } else {
                currentState.copy(isError = true)
            }
            _favoriteLiveData.postValue(newState)
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

    data class FavoriteState(
        val recipeList: List<Recipe> = emptyList(),
        val isError: Boolean = false,
    )
}