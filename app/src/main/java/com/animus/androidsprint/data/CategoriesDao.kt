package com.animus.androidsprint.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.animus.androidsprint.model.Category

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM category")
    fun getAll(): List<Category>

    @Insert
    fun insertCategory(category: Category)
}