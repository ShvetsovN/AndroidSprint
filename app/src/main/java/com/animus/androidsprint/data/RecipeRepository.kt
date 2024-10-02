package com.animus.androidsprint.data

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
            null
        }
    }

    fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        return try {
            val response = service.getRecipesByCategoryId(categoryId).execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    fun getCategoryById(categoryId: Int): Category? {
        return try {
            val response = service.getCategoryById(categoryId).execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    fun getRecipesByIds(categoryId: Int, recipeIds: Set<Int>): List<Recipe>? {
        return try {
            val response = service.getRecipesByIds(categoryId, recipeIds).execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    fun getCategories(): List<Category>? {
        return try {
            val response = service.getCategories().execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
}