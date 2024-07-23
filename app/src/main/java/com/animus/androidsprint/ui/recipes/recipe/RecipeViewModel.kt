package com.animus.androidsprint.ui.recipes.recipe

import androidx.lifecycle.ViewModel

data class RecipeState(
    var numberOfPortion: Int? = null,
    var quantity: String? = "",
    var isFavorite: Boolean? = null,
    )

class RecipeViewModel : ViewModel(){
}