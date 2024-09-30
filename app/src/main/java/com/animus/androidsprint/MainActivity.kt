package com.animus.androidsprint

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.animus.androidsprint.databinding.ActivityMainBinding
import com.animus.androidsprint.model.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val threadPool = Executors.newFixedThreadPool(10)
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null")

    private val navOption = NavOptions.Builder()
        .setLaunchSingleTop(true)
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val thread = Thread {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val request: Request = Request.Builder()
                .url("https://recipes.androidsprint.ru/api/category")
                .build()

            client.newCall(request).execute().use { response ->
                val gson = Gson()
                val listType = object : TypeToken<List<Category>>() {}.type
                val categories: List<Category> = gson.fromJson(response.body?.string(), listType)

                Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
                val categoryIds = categories.map { it.id }
                categoryIds.forEach { categoryId ->
                    threadPool.execute {
                        val recipeRequest = Request.Builder()
                            .url("https://recipes.androidsprint.ru/api/category/$categoryId/recipes")
                            .build()
                        client.newCall(recipeRequest).execute().use { recipeResponse ->
                            val recipeResponseBody = recipeResponse.body?.string()
                            Log.i(
                                "!!!",
                                "Для категории с id:$categoryId получены рецепты: $recipeResponseBody"
                            )
                        }
                    }
                }
            }
        }
        thread.start()

        Log.i("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        with(binding) {
            btnFavorites.setOnClickListener {
                findNavController(R.id.nav_host_fragment).navigate(
                    R.id.favoritesFragment,
                    null,
                    navOption
                )
            }
            btnCategory.setOnClickListener {
                findNavController(R.id.nav_host_fragment).navigate(
                    R.id.categoriesListFragment,
                    null,
                    navOption
                )
            }
        }
    }
}