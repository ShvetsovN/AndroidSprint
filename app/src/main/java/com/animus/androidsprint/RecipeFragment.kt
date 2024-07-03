package com.animus.androidsprint

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.animus.androidsprint.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private var recipe: Recipe? = null
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw throw IllegalStateException("Binding for FragmentRecipeBinding must not be null")

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        val view = binding.root

        arguments?.let {
            recipe = if (Build.VERSION.SDK_INT < 33) {
                it.getParcelable(Constants.ARG_RECIPE)
            } else {
                it.getParcelable(Constants.ARG_RECIPE, Recipe::class.java)
            }
        }
        recipe?.title?.let {
            binding.tvRecipeHeader.text = it
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}