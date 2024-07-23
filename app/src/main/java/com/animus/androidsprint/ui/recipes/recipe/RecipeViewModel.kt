package com.animus.androidsprint.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.animus.androidsprint.model.Recipe

class RecipeViewModel : ViewModel(){

    data class RecipeState(
        val recipe: Recipe? = null,
        val portionCount: Int = 1,
        val isFavorite: Boolean = false,
    )
}
