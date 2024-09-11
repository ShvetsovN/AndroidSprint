package com.animus.androidsprint.ui.recipes.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.animus.androidsprint.Constants
import com.animus.androidsprint.R
import com.animus.androidsprint.databinding.FragmentFavoritesBinding
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
        Log.e("FavoriteFragment", "created")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        binding.rvFavorites.adapter = favoriteAdapter
        viewModel.favoriteLiveData.observe(viewLifecycleOwner) { recipeState ->
            binding.tvEmptyText.isVisible = recipeState.recipeList.isEmpty()
            favoriteAdapter.dataSet = recipeState.recipeList
            favoriteAdapter.setOnItemClickListener(object : RecipeListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    Log.e("!!!", "initRecycler $recipeId")
                    openRecipe(recipeId)
                }
            })
        }
        viewModel.loadFavorites()
    }

    private fun openRecipe(recipeId: Int) {
        val bundle = Bundle().apply {
            putInt(Constants.ARG_RECIPE_ID, recipeId)
        }
        findNavController().navigate(R.id.action_favoritesFragment_to_recipeFragment, bundle)
    }
}


