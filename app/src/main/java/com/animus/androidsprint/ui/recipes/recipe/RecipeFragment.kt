package com.animus.androidsprint.ui.recipes.recipe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.animus.androidsprint.Constants
import com.animus.androidsprint.R
import com.animus.androidsprint.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment() : Fragment() {

    private val viewModel: RecipeViewModel by viewModels()
    private var recipeId: Int? = null
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw throw IllegalStateException("Binding for FragmentRecipeBinding must not be null")

    private val ingredientsAdapter = IngredientsAdapter()
    private val methodAdapter = MethodAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        Log.e("RecipeFragment", "created")
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
        initUI()
    }

    class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) : OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            onChangeIngredients(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.e("RecipeFragment", "destroy")
    }

    private fun initUI() {
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvMethod.adapter = methodAdapter

        val sizeInDp =
            resources.getDimensionPixelSize(R.dimen.cardview_item_ingredient_divider_horizontal_indent)
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

        viewModel.recipeLiveData.observe(viewLifecycleOwner) { recipeState ->
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
            val portionSeekBarListener = PortionSeekBarListener { progress ->
                viewModel.updatingPortionCount(progress)
            }
            binding.seekBar.setOnSeekBarChangeListener(portionSeekBarListener)

            with(binding) {
                tvRecipeHeader.text = recipeState.recipe?.title
                ivFragmentRecipeHeader.setImageDrawable(recipeState.recipeImage)
                ibFavoriteRecipe.setImageResource(
                    if (!recipeState.isFavorite) R.drawable.ic_heart_empty else R.drawable.ic_heart
                )
                ibFavoriteRecipe.setOnClickListener {
                    viewModel.onFavoritesClicked()
                }

                recipeState.recipe?.let { recipe ->
                    ingredientsAdapter.dataSet = recipe.ingredients
                    methodAdapter.dataSet = recipe.method
                }

                ingredientsAdapter.updateIngredients(recipeState.portionCount)
                tvNumberOfPortions.text = recipeState.portionCount.toString()
            }
        }
    }
}