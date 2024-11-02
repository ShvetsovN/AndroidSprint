package com.animus.androidsprint.di

import com.animus.androidsprint.data.RecipeRepository
import com.animus.androidsprint.ui.recipes.recipeList.RecipeListViewModel

class RecipesListViewModelFactory(
    private val recipeRepository: RecipeRepository
): Factory<RecipeListViewModel> {
    override fun create(): RecipeListViewModel {
        return RecipeListViewModel(recipeRepository)
    }
}