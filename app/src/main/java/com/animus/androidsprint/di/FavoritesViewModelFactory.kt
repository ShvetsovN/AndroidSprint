package com.animus.androidsprint.di

import com.animus.androidsprint.data.RecipeRepository
import com.animus.androidsprint.ui.recipes.favorites.FavoriteViewModel

class FavoritesViewModelFactory(
    private val recipeRepository: RecipeRepository
): Factory<FavoriteViewModel> {
    override fun create(): FavoriteViewModel {
        return FavoriteViewModel(recipeRepository)
    }
}