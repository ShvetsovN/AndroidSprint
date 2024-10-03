package com.animus.androidsprint

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.animus.androidsprint.databinding.ActivityMainBinding

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

        with(binding)
        {
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