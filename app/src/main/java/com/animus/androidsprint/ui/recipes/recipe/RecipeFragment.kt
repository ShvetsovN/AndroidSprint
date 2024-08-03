package com.animus.androidsprint.ui.recipes.recipe

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.animus.androidsprint.Constants
import com.animus.androidsprint.R
import com.animus.androidsprint.databinding.FragmentRecipeBinding
import com.animus.androidsprint.model.Recipe
import com.google.android.material.divider.MaterialDividerItemDecoration
import java.io.IOException
import java.io.InputStream

class RecipeFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels()
    private var recipeId: Int? = null
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw throw IllegalStateException("Binding for FragmentRecipeBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        Log.i("!!!", "Fragment created")
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            recipeId = it.getInt(Constants.ARG_RECIPE_ID)
        }
        recipeId?.let {
            viewModel.loadRecipe(it)
        }
        initRecycle()
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.i("!!!", "Fragment destroy")
    }

    private fun initRecycle() {
        val ingredientsAdapter = viewModel.recipeLiveData.value?.recipe?.ingredients?.let {
            IngredientsAdapter(it)
        }
        val sizeInDp =
            resources.getDimensionPixelSize(R.dimen.cardview_item_ingredient_divider_horizontal_indent)
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvMethod.adapter = viewModel.recipeLiveData.value?.recipe?.method?.let {
            MethodAdapter(it)
        }
        val itemDecorationIngredient = MaterialDividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        )
        val itemDecorationMethod =
            MaterialDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        binding.rvIngredients.addItemDecoration(itemDecorationIngredient)
        binding.rvMethod.addItemDecoration(itemDecorationMethod)
        viewModel.recipeLiveData.observe(viewLifecycleOwner) { state ->
            state?.let {
                val recipe: Recipe? = it.recipe
                recipe?.let {
                    itemDecorationIngredient.apply {
                        isLastItemDecorated = false
                        dividerInsetStart = sizeInDp
                        dividerInsetEnd = sizeInDp
                        setDividerColorResource(
                            requireContext(),
                            R.color.cardview_item_ingredient_divider_color
                        )
                    }
                    itemDecorationMethod.apply {
                        isLastItemDecorated = false
                        dividerInsetStart = sizeInDp
                        dividerInsetEnd = sizeInDp
                        setDividerColorResource(
                            requireContext(),
                            R.color.cardview_item_ingredient_divider_color
                        )
                    }
                    binding.seekBar.setOnSeekBarChangeListener(object :
                        SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(
                            seekBar: SeekBar?,
                            progress: Int,
                            fromUser: Boolean
                        ) {
                            ingredientsAdapter?.updateIngredients(progress)
                            binding.tvNumberOfPortions.text = progress.toString()
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                    })
                }
            }
        }
    }

    private fun initUI() {
        viewModel.recipeLiveData.observe(viewLifecycleOwner) { state ->
            with(binding) {
                tvRecipeHeader.text = state.recipe?.title
                ivFragmentRecipeHeader.setImageDrawable(state.recipeImage)
                ibFavoriteRecipe.setImageResource(
                    if (!state.isFavorite) R.drawable.ic_heart_empty else R.drawable.ic_heart
                )
                ibFavoriteRecipe.setOnClickListener {
                    viewModel.onFavoritesClicked()
                }
            }
        }
    }
}




