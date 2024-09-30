package com.animus.androidsprint.data

import com.animus.androidsprint.model.Category
import com.animus.androidsprint.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {
    @GET("recipe/{id}")
    fun getRecipeById(@Path("id") recipeId: Int): Call<Recipe>

    @GET("recipes")
    fun getRecipesByCategoryId(@Query("categoryId") categoryId: Int): Call<List<Recipe>>

    @GET("category/{id}")
    fun getCategoryById(@Path("id") categoryId: Int): Call<Category>

    @GET("category/{id}/recipes")
    fun getRecipesByIds(
        @Query("recipeIds") recipeIds: Set<Int>
    ): Call<List<Recipe>>

    @GET("category")
    fun getCategories(): Call<List<Category>>

}