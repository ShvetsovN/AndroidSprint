package com.animus.androidsprint.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.animus.androidsprint.Constants
import com.animus.androidsprint.model.Category
import com.animus.androidsprint.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class RecipeRepository (context: Context) {

    private val appContext = context.applicationContext
    private val contentType = Constants.CONTENT_TYPE.toMediaType()
    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    private var service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    private val db: AppDatabase = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        "database-categories"
    ).build()

    private val categoriesDao = db.categoryDao()
    private val recipeDao = db.recipeDao()

    suspend fun getFavoriteRecipes(): List<Recipe>?{
        return withContext(Dispatchers.IO) {
            try {
                recipeDao.getFavoriteRecipes()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun updateFavoriteStatus(recipeId: Int, isFavorite: Boolean) {
        return withContext(Dispatchers.IO) {
            try {
                recipeDao.updateFavoriteStatus(recipeId, isFavorite)
            } catch (e: Exception) {
                Log.e("RecipeRepository", "Error updating favorite status: ${e.message}")
            }
        }
    }

    suspend fun getRecipesFromCacheByCategoryId(categoryId: Int): List<Recipe> {
        return withContext(Dispatchers.IO) {
            try {
                recipeDao.getRecipesByCategoryId(categoryId)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun getRecipesFromCache(): List<Recipe> {
        return withContext(Dispatchers.IO) {
            try {
                recipeDao.getAll()
            } catch (e: Exception) {
                Log.e("RecipeRepository", "Error fetching recipe from cache: ${e.message}")
                emptyList()
            }
        }
    }

    suspend fun saveRecipesToCache(recipes: List<Recipe>, categoryId: Int) {
        val updatedRecipes = recipes.map { it.copy(categoryId = categoryId) }
        withContext(Dispatchers.IO) {
            try {
                recipeDao.insertAll(updatedRecipes)
            } catch (e: Exception) {
                Log.e("RecipeRepository", "Error saving categories to cache: ${e.message}")
            }
        }
    }

    suspend fun getCategoriesFromCache(): List<Category> {
        return withContext(Dispatchers.IO) {
            try {
                categoriesDao.getAll()
            } catch (e: Exception) {
                Log.e("RecipeRepository", "Error fetching categories from cache: ${e.message}")
                emptyList()
            }
        }
    }

    suspend fun saveCategoriesToCache(categories: List<Category>) {
        withContext(Dispatchers.IO) {
            try {
                categoriesDao.insertAll(categories)
            } catch (e: Exception) {
                Log.e("RecipeRepository", "Error saving categories to cache: ${e.message}")
            }
        }
    }

    suspend fun getRecipeById(recipeId: Int): Recipe? {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getRecipeById(recipeId).execute()
                if (response.isSuccessful) response.body() else null
            } catch (e: Exception) {
                Log.i("RecipeRepository", "getRecipesByCategoryId error: ${e.message}")
                null
            }
        }
    }

    suspend fun getRecipesByCategoryId(recipeIdsSet: Set<String>): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            try {
                val recipeIdsString = recipeIdsSet.joinToString(",")
                val response = service.getRecipesByCategoryId(recipeIdsString).execute()
                Log.i(
                    "RecipeRepository",
                    "Response getRecipesByCategoryId code: ${response.code()}"
                )
                if (response.isSuccessful) response.body() else null
            } catch (e: Exception) {
                Log.i("RecipeRepository", "getRecipesByCategoryId error: ${e.message}")
                null
            }
        }
    }

    suspend fun getCategoryById(categoryId: Int): Category? {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getCategoryById(categoryId).execute()
                if (response.isSuccessful) response.body() else null
            } catch (e: Exception) {
                Log.i("RecipeRepository", "getCategoryById error: ${e.message}")
                null
            }
        }
    }

    suspend fun getRecipesByIds(categoryId: Int): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getRecipesByIds(categoryId).execute()
                if (response.isSuccessful) response.body() else null
            } catch (e: Exception) {
                Log.i("RecipeRepository", "getRecipesByIds error: ${e.message}")
                null
            }
        }
    }

    suspend fun getCategories(): List<Category>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getCategories().execute()
                if (response.isSuccessful) response.body() else null
            } catch (e: Exception) {
                Log.i("RecipeRepository", "getCategories error: ${e.message}")
                null
            }
        }
    }
}