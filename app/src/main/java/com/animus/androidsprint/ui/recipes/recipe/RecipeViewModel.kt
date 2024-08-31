package com.animus.androidsprint.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.animus.androidsprint.Constants
import com.animus.androidsprint.data.STUB
import com.animus.androidsprint.model.Recipe
import java.io.IOException
import java.io.InputStream

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val _recipeLiveData = MutableLiveData<RecipeState>()
    val recipeLiveData: LiveData<RecipeState> = _recipeLiveData

    override fun onCleared() {
        Log.i("!!!", "Recipe VM cleared")
        super.onCleared()
    }

    fun updatingPortionCount(portionCount: Int) {
        _recipeLiveData.value = _recipeLiveData.value?.copy(portionCount = portionCount)
    }

    fun loadRecipe(recipeId: Int) {
        //TODO ("load from network")
        val recipe = STUB.getRecipeById(recipeId)
        val favorites = getFavorites().contains(recipeId.toString())
        var drawable: Drawable? = null
        try {
            val inputStream: InputStream? =
                recipe?.imageUrl?.let {
                    getApplication<Application>().assets.open(it)
                }
            drawable = Drawable.createFromStream(inputStream, null)
            inputStream?.close()
        } catch (ex: IOException) {
            Log.e("!!!", "Error loading image from assets (RVM.loadRecipe)", ex)
        }

        _recipeLiveData.value = RecipeState(
            recipe = recipe,
            portionCount = recipeLiveData.value?.portionCount ?: 1,
            isFavorite = favorites,
            recipeImage = drawable
        )
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
        val recipeImage: Drawable?,
    )
}
