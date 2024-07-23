package com.animus.androidsprint.ui.recipes.recipe

import androidx.lifecycle.ViewModel

data class RecipeState(
    var numberOfPortion: Int? = null,
    var quantity: String? = "",
    var isFavorite: Boolean? = null,
    var imageUrl: String? = "",
    var title: String? = "",
    var seekBar: Int? = null,
    var id: Int? = null,
)

class RecipeViewModel : ViewModel()
