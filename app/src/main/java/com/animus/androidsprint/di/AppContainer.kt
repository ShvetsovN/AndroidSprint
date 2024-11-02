package com.animus.androidsprint.di

import android.content.Context
import androidx.room.Room
import com.animus.androidsprint.Constants
import com.animus.androidsprint.data.AppDatabase
import com.animus.androidsprint.data.RecipeApiService
import com.animus.androidsprint.data.RecipeRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class AppContainer(context: Context) {

    private val db: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "database-categories"
    ).build()

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val categoriesDao = db.categoryDao()
    private val recipeDao = db.recipeDao()

    private val contentType = Constants.CONTENT_TYPE.toMediaType()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    private val recipeApiService: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    private val repository = RecipeRepository(
        recipeDao = recipeDao,
        categoriesDao = categoriesDao,
        recipeApiService = recipeApiService,
        ioDispatcher = ioDispatcher,
    )

    val categoriesListViewModelFactory = CategoriesListViewModelFactory(repository)
    val recipesListViewModelFactory = RecipesListViewModelFactory(repository)
    val recipeViewModelFactory = RecipeViewModelFactory(repository)
    val favoritesViewModelFactory = FavoritesViewModelFactory(repository)

}
