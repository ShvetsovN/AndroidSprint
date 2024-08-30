package com.animus.androidsprint.ui.recipes.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.animus.androidsprint.Constants
import com.animus.androidsprint.R
import com.animus.androidsprint.data.STUB
import com.animus.androidsprint.databinding.FragmentFavoritesBinding
import com.animus.androidsprint.ui.recipes.recipe.RecipeFragment
import com.animus.androidsprint.ui.recipes.recipeList.RecipeListAdapter

class FavoritesFragment : Fragment() {

    private val viewModel: FavoriteViewModel by viewModels()

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFavoritesBinding must not be null")

    private val favoriteAdapter = RecipeListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root
        Log.e("!!!", "Favorites Fragment created")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        binding.rvFavorites.adapter = favoriteAdapter
        viewModel.favoriteLiveData.observe(viewLifecycleOwner) {
            binding.tvEmptyText.isVisible = it.recipeList.isEmpty()
            favoriteAdapter.setOnItemClickListener(object : RecipeListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            })
        }
        viewModel.loadFavorites()
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        val bundle = Bundle().apply {
            putParcelable(Constants.ARG_RECIPE, recipe)
        }
        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}


