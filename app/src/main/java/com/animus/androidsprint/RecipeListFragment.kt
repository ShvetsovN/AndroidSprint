package com.animus.androidsprint

import android.os.Bundle
import android.text.TextUtils.replace
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.RecyclerView
import com.animus.androidsprint.databinding.FragmentListCategoriesBinding
import com.animus.androidsprint.databinding.FragmentRecipeBinding
import com.animus.androidsprint.databinding.FragmentRecipeListBinding

class RecipeListFragment : Fragment() {

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null
    private var _binding: FragmentRecipeListBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentRecipeListBinding must not be null")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        _binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.ivHeaderRecipeList.setImageResource(R.drawable.bcg_categories)
        initRecycle()

        arguments?.let {
            categoryId = it.getInt(Constants.ARG_CATEGORY_ID)
            categoryName = it.getString(Constants.ARG_CATEGORY_NAME)
            categoryImageUrl = it.getString(Constants.ARG_CATEGORY_IMAGE_URL)
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun initRecycle() {
        val adapter = categoryId?.let { STUB.getRecipesByCategoryId(it) }
            ?.let { RecipeListAdapter(it) }
        val recyclerView: RecyclerView = binding.rvRecipes
        recyclerView.adapter = adapter
        adapter?.setOnItemClickListener(object : RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }
    fun openRecipeByRecipeId(recipeId: Int) {
        val id = categoryId ?: return
        val recipe = STUB.getRecipesByCategoryId(id).find { it.id == recipeId }
        val recipeName = recipe?.title
        val recipeIngredient = recipe?.ingredients
        val recipeMethod = recipe?.method
        val recipeImageUrl = recipe?.imageUrl

        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}