package com.animus.androidsprint

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.animus.androidsprint.databinding.ActivityMainBinding
import com.animus.androidsprint.ui.categories.CategoriesListFragment
import com.animus.androidsprint.ui.recipes.favorites.FavoritesFragment

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<CategoriesListFragment>(R.id.mainContainer)
            }
        }

        with(binding) {
            btnFavorites.setOnClickListener {
                supportFragmentManager.commit {
                    replace<FavoritesFragment>(R.id.mainContainer)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
            btnCategory.setOnClickListener {
                supportFragmentManager.commit {
                    replace<CategoriesListFragment>(R.id.mainContainer)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
        }
    }
}
