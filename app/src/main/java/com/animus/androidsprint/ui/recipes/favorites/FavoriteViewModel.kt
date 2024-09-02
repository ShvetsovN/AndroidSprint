package com.animus.androidsprint.ui.recipes.favorites

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.animus.androidsprint.Constants
import com.animus.androidsprint.data.STUB
import com.animus.androidsprint.model.Recipe

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val _favoriteLiveData = MutableLiveData<FavoriteState>()
    val favoriteLiveData: LiveData<FavoriteState> = _favoriteLiveData

    init {
        Log.e("!!!", "VM favorite created")
    }

    fun loadFavorites() {
        val favorites: MutableSet<String> = getFavorites()
        val favoritesRecipe: List<Recipe> =
            STUB.getRecipesByIds(favorites.map { it.toInt() }.toSet())
        Log.d("!!!", "Loaded favorite recipes: $favoritesRecipe")
        _favoriteLiveData.value = FavoriteState(recipeList = favoritesRecipe)
    }

    fun getRecipeById(recipeId: Int) = STUB.getRecipeById(recipeId)

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
        val recipeList: List<Recipe> = emptyList()
    )
}