package com.animus.androidsprint

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.animus.androidsprint.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFavoritesBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        val sharedPrefs = activity?.getSharedPreferences(Constants.PREFERENCE_RECIPE_NAME, Context.MODE_PRIVATE)
        val favorites = sharedPrefs?.getStringSet(Constants.PREFERENCE_RECIPE_FAVORITES, emptySet())
        val favoritesToInt = favorites?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        val favoriteRecipes = STUB.getRecipesByIds(favoritesToInt)
        val adapter = RecipeListAdapter(favoriteRecipes)
        binding.rvFavorites.adapter = adapter
        if (favoriteRecipes.isEmpty()) {
           binding.tvEmptyText.visibility = View.VISIBLE
        } else {
            binding.tvEmptyText.visibility = View.GONE
        }
        adapter.setOnItemClickListener(object : RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
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
