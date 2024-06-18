package com.animus.androidsprint

data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: Ingredient,
    val method: String,
    val imageUrl: String,
)
