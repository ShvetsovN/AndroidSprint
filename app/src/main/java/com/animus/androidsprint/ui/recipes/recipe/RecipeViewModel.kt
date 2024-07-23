package com.animus.androidsprint.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.animus.androidsprint.model.Ingredient

data class RecipeState(
    var numberOfPortion: Int? = null,
    var ingredients: List<Ingredient>? = listOf(),
    var imageUrl: String? = null,
    )

data class Ingredient(
    val quantity: String? = "",
)

class RecipeViewModel : ViewModel(){
}