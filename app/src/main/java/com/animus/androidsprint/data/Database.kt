package com.animus.androidsprint.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.animus.androidsprint.model.Category
import com.animus.androidsprint.model.Recipe

@Database(
    entities = [Category::class, Recipe::class],
    version = 2,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoriesDao
    abstract fun recipesDao(): RecipesDao
}