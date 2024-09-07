package com.animus.androidsprint.ui.recipes.recipeList

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.animus.androidsprint.Constants
import com.animus.androidsprint.R
import com.animus.androidsprint.databinding.FragmentRecipeListBinding
import java.io.IOException
import java.io.InputStream

class RecipeListFragment : Fragment() {

    private val viewModel: RecipeListViewModel by viewModels()
    private val recipeListAdapter = RecipeListAdapter()
    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null
    private var _binding: FragmentRecipeListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeListBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            categoryId = it.getInt(Constants.ARG_CATEGORY_ID)
            categoryName = it.getString(Constants.ARG_CATEGORY_NAME)
            categoryImageUrl = it.getString(Constants.ARG_CATEGORY_IMAGE_URL)
        }
        categoryName?.let {
            binding.tvHeaderRecipeList.text = it
        }
        categoryImageUrl?.let { imageUrl ->
            try {
                val inputStream: InputStream =
                    binding.ivFragmentRecipeListHeader.context.assets.open(imageUrl)
                val drawable = Drawable.createFromStream(inputStream, null)
                binding.ivFragmentRecipeListHeader.setImageDrawable(drawable)
            } catch (ex: IOException) {
                Log.e("RecipeListFragment onViewCreated", "Error loading image from assets")
            }
        }
        categoryId?.let { viewModel.loadRecipe(it) }
        initRecycle()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycle() {
        binding.rvRecipes.adapter = recipeListAdapter
        viewModel.recipeListLiveData.observe(viewLifecycleOwner) {
            recipeListAdapter.dataSet = it.recipeList
        }
        recipeListAdapter.setOnItemClickListener(object : RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = Bundle().apply {
            putInt(Constants.ARG_RECIPE_ID, recipeId)
        }
        findNavController().navigate(R.id.recipeFragment, bundle)
    }
}