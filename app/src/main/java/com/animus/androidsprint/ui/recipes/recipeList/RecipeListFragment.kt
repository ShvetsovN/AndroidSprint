package com.animus.androidsprint.ui.recipes.recipeList

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.animus.androidsprint.Constants
import com.animus.androidsprint.databinding.FragmentRecipeListBinding
import java.io.IOException
import java.io.InputStream

class RecipeListFragment : Fragment() {

    private val viewModel: RecipeListViewModel by viewModels()
    private val args: RecipeListFragmentArgs by navArgs()
    private val recipeListAdapter = RecipeListAdapter()
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
        val category = args.category
        binding.tvHeaderRecipeList.text = category.title
        try {
            val inputStream: InputStream =
                binding.ivFragmentRecipeListHeader.context.assets.open(category.imageUrl)
            val drawable = Drawable.createFromStream(inputStream, null)
            binding.ivFragmentRecipeListHeader.setImageDrawable(drawable)
        } catch (ex: IOException) {
            Log.e("RecipeListFragment onViewCreated", "Error loading image from assets")
        }
        viewModel.loadRecipe(category.id)
        initRecycle()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycle() {
        binding.rvRecipes.adapter = recipeListAdapter
        recipeListAdapter.setOnItemClickListener(object : RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
        viewModel.recipeListLiveData.observe(viewLifecycleOwner) { recipeState ->
            if (recipeState.isError) {
                Toast.makeText(context, Constants.TOAST_ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
            } else {
                recipeListAdapter.dataSet = recipeState.recipeList
            }
        }
    }

    fun openRecipeByRecipeId(recipeId: Int) {
        findNavController().navigate(
            RecipeListFragmentDirections.actionRecipeListFragmentToRecipeFragment(
                recipeId
            )
        )
    }
}