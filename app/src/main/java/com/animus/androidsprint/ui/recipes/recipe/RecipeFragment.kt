package com.animus.androidsprint.ui.recipes.recipe

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
import com.google.android.material.divider.MaterialDividerItemDecoration

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
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.i("!!!", "Fragment destroy")
    }

    private fun initUI() {
        val ingredientsAdapter = viewModel.recipeLiveData.value?.recipe?.ingredients?.let { IngredientsAdapter(it) }
        binding.rvIngredients.adapter = ingredientsAdapter

        val methodAdapter = viewModel.recipeLiveData.value?.recipe?.method?.let { MethodAdapter(it) }
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
            binding.seekBar.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    viewModel.updatingPortionCount(progress)
                    ingredientsAdapter?.updateIngredients(progress)
                    binding.tvNumberOfPortions.text = progress.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
            with(binding) {
                tvRecipeHeader.text = recipeState.recipe?.title
                ivFragmentRecipeHeader.setImageDrawable(recipeState.recipeImage)
                ibFavoriteRecipe.setImageResource(
                    if (!recipeState.isFavorite) R.drawable.ic_heart_empty else R.drawable.ic_heart
                )
                ibFavoriteRecipe.setOnClickListener {
                    viewModel.onFavoritesClicked()
                }
            }
        }
    }
}