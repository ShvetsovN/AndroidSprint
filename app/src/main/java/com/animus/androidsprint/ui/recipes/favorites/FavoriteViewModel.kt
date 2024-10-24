package com.animus.androidsprint.ui.recipes.favorites

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.animus.androidsprint.data.RecipeRepository
import com.animus.androidsprint.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RecipeRepository(application)
    private val _favoriteLiveData = MutableLiveData<FavoriteState>()
    val favoriteLiveData: LiveData<FavoriteState> = _favoriteLiveData

    init {
        Log.e("FavoriteVM", "VM favorite created")
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch(Dispatchers.Default) {
            val currentState = _favoriteLiveData.value ?: FavoriteState()
            val favoritesRecipe: List<Recipe>? = repository.getFavoriteRecipes()
            Log.i("FavoriteVM", "Loaded favorite recipes: $favoritesRecipe")

            val newState = if (favoritesRecipe != null) {
                currentState.copy(recipeList = favoritesRecipe, isError = false)
            } else {
                currentState.copy(isError = true)
            }
            _favoriteLiveData.postValue(newState)
        }
    }

    data class FavoriteState(
        val recipeList: List<Recipe> = emptyList(),
        val isError: Boolean = false,
    )
}