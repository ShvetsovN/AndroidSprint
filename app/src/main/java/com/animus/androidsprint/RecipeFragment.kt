package com.animus.androidsprint

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.animus.androidsprint.databinding.FragmentRecipeBinding
import java.io.IOException
import java.io.InputStream

class RecipeFragment : Fragment() {

    private var recipe: Recipe? = null
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw throw IllegalStateException("Binding for FragmentRecipeBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
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
        initRecycle()
        iniUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycle() {
        val contextIngredients = binding.rvIngredients.context
        val contextMethod = binding.rvMethod.context
        recipe?.let {
            binding.rvIngredients.adapter = IngredientsAdapter(it.ingredients)
            binding.rvIngredients.addItemDecoration(
                DividerItemDecoration(
                    contextIngredients,
                    LinearLayoutManager.VERTICAL
                )
            )
            binding.rvMethod.adapter = MethodAdapter(it.method)
            binding.rvMethod.addItemDecoration(
                DividerItemDecoration(
                    contextMethod,
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    private fun iniUI() {
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
        }
    }
}