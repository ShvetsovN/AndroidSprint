package com.animus.androidsprint.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable @Entity
data class Recipe(
    @PrimaryKey val id: Int,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("ingredients") val ingredients: List<Ingredient>,
    @ColumnInfo("method") val method: List<String>,
    @ColumnInfo("imageUrl") val imageUrl: String,
    @ColumnInfo("categoryId") val categoryId: Int = -1,
) : java.io.Serializable
