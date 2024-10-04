package com.animus.androidsprint.data

import android.util.Log
import com.animus.androidsprint.Constants
import com.animus.androidsprint.model.Category
import com.animus.androidsprint.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class RecipeRepository {

    private val contentType = Constants.CONTENT_TYPE.toMediaType()
    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    private var service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    fun getRecipeById(recipeId: Int): Recipe? {
        return try {
            val response = service.getRecipeById(recipeId).execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            Log.i("RecipeRepository", "getRecipesByCategoryId error: ${e.message}")
            null
        }
    }

    fun getRecipesByCategoryId(recipeIdsSet: Set<String>): List<Recipe>? {
        return try {
            val recipeIdsString = recipeIdsSet.joinToString(",")
            val response = service.getRecipesByCategoryId(recipeIdsString).execute()
            Log.i("RecipeRepository", "Response getRecipesByCategoryId code: ${response.code()}")
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            Log.i("RecipeRepository", "getRecipesByCategoryId error: ${e.message}")
            null
        }
    }

    fun getCategoryById(categoryId: Int): Category? {
        return try {
            val response = service.getCategoryById(categoryId).execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            Log.i("RecipeRepository", "getCategoryById error: ${e.message}")
            null
        }
    }

    fun getRecipesByIds(categoryId: Int): List<Recipe>? {
        return try {
            val response = service.getRecipesByIds(categoryId).execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            Log.i("RecipeRepository", "getRecipesByIds error: ${e.message}")
            null
        }
    }

    fun getCategories(): List<Category>? {
        return try {
            val response = service.getCategories().execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            Log.i("RecipeRepository", "getCategories error: ${e.message}")
            null
        }
    }
}