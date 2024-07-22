package com.animus.androidsprint.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.animus.androidsprint.model.Ingredient

data class RecipeState(
    var id: Int? = null,
    var title: String? = "",
    var ingredients: List<Ingredient>? = listOf(),
    var method: List<String>? = listOf(),
    var imageUrl: String? = "",
    )

data class Ingredient(
    val quantity: String? = "",
    val unitOfMeasure: String? = "",
    val description: String? = "",
)

class RecipeViewModel : ViewModel(){
}