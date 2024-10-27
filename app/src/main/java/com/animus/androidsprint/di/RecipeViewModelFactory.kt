package com.animus.androidsprint.di

import com.animus.androidsprint.data.RecipeRepository
import com.animus.androidsprint.ui.recipes.recipe.RecipeViewModel

class RecipeViewModelFactory(
    private val recipeRepository: RecipeRepository
): Factory<RecipeViewModel> {
    override fun create(): RecipeViewModel {
        return RecipeViewModel(recipeRepository)
    }
}