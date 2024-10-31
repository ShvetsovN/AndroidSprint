package com.animus.androidsprint.data

import android.util.Log
import com.animus.androidsprint.model.Category
import com.animus.androidsprint.model.Recipe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RecipeRepository(
    private val recipeDao: RecipeDao,
    private val categoriesDao: CategoriesDao,
    private val recipeApiService: RecipeApiService,
    private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun getFavoriteRecipes(): List<Recipe>? {
        return withContext(ioDispatcher) {
            try {
                recipeDao.getFavoriteRecipes()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun updateFavoriteStatus(recipeId: Int, isFavorite: Boolean) {
        return withContext(ioDispatcher) {
            try {
                recipeDao.updateFavoriteStatus(recipeId, isFavorite)
            } catch (e: Exception) {
                Log.e("RecipeRepository", "Error updating favorite status: ${e.message}")
            }
        }
    }

    suspend fun getRecipesFromCacheByCategoryId(categoryId: Int): List<Recipe> {
        return withContext(ioDispatcher) {
            try {
                recipeDao.getRecipesByCategoryId(categoryId)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun saveRecipesToCache(recipes: List<Recipe>, categoryId: Int) {
        val updatedRecipes = recipes.map { it.copy(categoryId = categoryId) }
        withContext(ioDispatcher) {
            try {
                recipeDao.insertAll(updatedRecipes)
            } catch (e: Exception) {
                Log.e("RecipeRepository", "Error saving categories to cache: ${e.message}")
            }
        }
    }

    suspend fun getCategoriesFromCache(): List<Category> {
        return withContext(ioDispatcher) {
            try {
                categoriesDao.getAll()
            } catch (e: Exception) {
                Log.e("RecipeRepository", "Error fetching categories from cache: ${e.message}")
                emptyList()
            }
        }
    }

    suspend fun saveCategoriesToCache(categories: List<Category>) {
        withContext(ioDispatcher) {
            try {
                categoriesDao.insertAll(categories)
            } catch (e: Exception) {
                Log.e("RecipeRepository", "Error saving categories to cache: ${e.message}")
            }
        }
    }

    suspend fun getRecipeByIdFromCache(recipeId: Int): Recipe? {
        return withContext(ioDispatcher) {
            try {
                recipeDao.getRecipeById(recipeId)
            } catch (e: Exception) {
                Log.i("RecipeRepository", "getRecipesByCategoryId error: ${e.message}")
                null
            }
        }
    }

    suspend fun getRecipeFromServerById(recipeId: Int): Recipe? {
        return withContext(ioDispatcher) {
            try {
                val response = recipeApiService.getRecipeById(recipeId).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipesByIds(categoryId: Int): List<Recipe>? {
        return withContext(ioDispatcher) {
            try {
                val response = recipeApiService.getRecipesByIds(categoryId).execute()
                if (response.isSuccessful) response.body() else null
            } catch (e: Exception) {
                Log.i("RecipeRepository", "getRecipesByIds error: ${e.message}")
                null
            }
        }
    }

    suspend fun getCategories(): List<Category>? {
        return withContext(ioDispatcher) {
            try {
                val response = recipeApiService.getCategories().execute()
                if (response.isSuccessful) response.body() else null
            } catch (e: Exception) {
                Log.i("RecipeRepository", "getCategories error: ${e.message}")
                null
            }
        }
    }
}