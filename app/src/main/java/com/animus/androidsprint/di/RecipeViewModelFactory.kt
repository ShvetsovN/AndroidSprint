package com.animus.androidsprint.di

import com.animus.androidsprint.data.RecipeRepository
import com.animus.androidsprint.ui.recipes.recipe.RecipeViewModel

class RecipeViewModelFactory(
    private val recipeRepository: RecipeRepository
): Factory<RecipeViewModel> {

    private var recipeViewModel : RecipeViewModel? = null

    override fun create(): RecipeViewModel {
        if(recipeViewModel == null) recipeViewModel = RecipeViewModel(recipeRepository)
        return recipeViewModel as RecipeViewModel
    }
}