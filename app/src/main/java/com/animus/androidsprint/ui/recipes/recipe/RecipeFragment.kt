package com.animus.androidsprint.ui.recipes.recipe

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
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
import com.animus.androidsprint.model.Recipe
import com.animus.androidsprint.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import java.io.IOException
import java.io.InputStream

class RecipeFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels()
    private var recipe: Recipe? = null
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw throw IllegalStateException("Binding for FragmentRecipeBinding must not be null")

    private fun saveFavorites(favoriteRecipeId: Set<String>) {
        val sharedPrefs =
            activity?.getSharedPreferences(Constants.PREFERENCE_RECIPE_NAME, Context.MODE_PRIVATE)
                ?: return
        with(sharedPrefs.edit()) {
            putStringSet(Constants.PREFERENCE_RECIPE_FAVORITES, favoriteRecipeId)
            apply()
        }
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs =
            activity?.getSharedPreferences(Constants.PREFERENCE_RECIPE_NAME, Context.MODE_PRIVATE)
        val savedSet =
            sharedPrefs?.getStringSet(Constants.PREFERENCE_RECIPE_FAVORITES, mutableSetOf())
                ?: mutableSetOf()
        return HashSet(savedSet)
    }

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
            recipe = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(Constants.ARG_RECIPE)
            } else {
                it.getParcelable(Constants.ARG_RECIPE, Recipe::class.java)
            }
        }
        viewModel.recipeLiveData.observe(viewLifecycleOwner){

        }
        viewModel.portionLiveData.observe(viewLifecycleOwner){

        }
        viewModel.isFavoriteLiveData.observe(viewLifecycleOwner) {
            Log.i("!!!", "$it")
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
        val contextIngredients = binding.rvIngredients.context
        val contextMethod = binding.rvMethod.context
        recipe?.let {
            val ingredientAdapter = IngredientsAdapter(it.ingredients)
            val sizeInDp =
                resources.getDimensionPixelSize(R.dimen.cardview_item_ingredient_divider_horizontal_indent)
            binding.rvIngredients.adapter = ingredientAdapter
            val itemDecorationIngredient = MaterialDividerItemDecoration(
                contextIngredients,
                LinearLayoutManager.VERTICAL
            ).apply {
                isLastItemDecorated = false
                dividerInsetStart = sizeInDp
                dividerInsetEnd = sizeInDp
                setDividerColorResource(
                    contextIngredients,
                    R.color.cardview_item_ingredient_divider_color
                )
            }
            binding.rvIngredients.addItemDecoration(itemDecorationIngredient)
            binding.rvMethod.adapter = MethodAdapter(it.method)
            val itemDecorationMethod =
                MaterialDividerItemDecoration(contextMethod, LinearLayoutManager.VERTICAL).apply {
                    isLastItemDecorated = false
                    dividerInsetStart = sizeInDp
                    dividerInsetEnd = sizeInDp
                    setDividerColorResource(
                        contextMethod,
                        R.color.cardview_item_ingredient_divider_color
                    )
                }
            binding.rvMethod.addItemDecoration(itemDecorationMethod)
            binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    ingredientAdapter.updateIngredients(progress)
                    binding.tvNumberOfPortions.text = progress.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

        }
    }

    private fun initUI() {
        recipe?.let { recipe ->
            binding.tvRecipeHeader.text = recipe.title
            binding.ivFragmentRecipeHeader.let {
                try {
                    val inputStream: InputStream =
                        it.context.assets.open(recipe.imageUrl)
                    val drawable = Drawable.createFromStream(inputStream, null)
                    it.setImageDrawable(drawable)
                } catch (ex: IOException) {
                    Log.e("RF.initUI", "Error loading image from assets")
                }
            }
            binding.ibFavoriteRecipe.apply {
                val favorites = getFavorites()
                val recipeId = recipe.id.toString()
                var isFavorite = favorites.contains(recipeId)
                setImageResource(if (isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_empty)
                setOnClickListener {
                    isFavorite = !isFavorite
                    if (isFavorite) {
                        favorites.add(recipeId)
                    } else {
                        favorites.remove(recipeId)
                    }
                    setImageResource(if (isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_empty)
                    saveFavorites(favorites)
                }
            }
        }
    }
}


