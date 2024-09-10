package com.animus.androidsprint

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.animus.androidsprint.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnFavorites.setOnClickListener {
                val navController = findNavController(R.id.nav_host_fragment)
                if (navController.currentDestination?.id != R.id.favoritesFragment) {
                    navController.navigate(R.id.favoritesFragment)
                }
            }
            btnCategory.setOnClickListener {
                val navController = findNavController(R.id.nav_host_fragment)
                if (navController.currentDestination?.id != R.id.categoriesListFragment) {
                    navController.navigate(R.id.categoriesListFragment)
                }
            }
        }
    }
}