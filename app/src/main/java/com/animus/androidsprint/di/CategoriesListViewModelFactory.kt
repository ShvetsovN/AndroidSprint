package com.animus.androidsprint.di

import com.animus.androidsprint.data.RecipeRepository
import com.animus.androidsprint.ui.categories.CategoriesListViewModel

class CategoriesListViewModelFactory(
    private val recipesRepository: RecipeRepository
): Factory<CategoriesListViewModel> {
    override fun create(): CategoriesListViewModel {
        return CategoriesListViewModel(recipesRepository)
    }
}