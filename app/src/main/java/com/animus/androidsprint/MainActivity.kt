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
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

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
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            val gson = Gson()
            val listType = object : TypeToken<List<Category>>() {}.type

            val categories: List<Category> = gson.fromJson(
                connection.inputStream.bufferedReader().readText(),
                listType
            )

            Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
            categories.forEach { category ->
                Log.i("!!!", "Название категории: ${category.title}")
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