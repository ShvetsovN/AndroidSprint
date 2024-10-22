package com.animus.androidsprint.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.animus.androidsprint.model.Category
import com.animus.androidsprint.model.Recipe

@Database(
    entities = [
        Category::class,
        Recipe::class,
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoriesDao
    abstract fun recipeDao(): RecipeDao
}