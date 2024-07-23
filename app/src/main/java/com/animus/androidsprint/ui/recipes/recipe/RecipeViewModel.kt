package com.animus.androidsprint.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.animus.androidsprint.model.Recipe

data class RecipeState(
    var recipe: Recipe? = null,
)

class RecipeViewModel : ViewModel()
