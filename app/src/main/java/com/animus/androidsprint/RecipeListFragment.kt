package com.animus.androidsprint

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
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
import java.io.IOException
import java.io.InputStream

class RecipeListFragment : Fragment() {

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
        categoryImageUrl?.let{ imageUrl ->
            try {
                val inputStream: InputStream = binding.ivHeaderRecipeList.context.assets.open(imageUrl)
                val drawable = Drawable.createFromStream(inputStream, null)
                binding.ivHeaderRecipeList.setImageDrawable(drawable)
            } catch (ex: IOException){
                Log.e("RLF.onViewCreated", "Error loading image from assets")
            }
        }
        initRecycle()
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
        val recipe = categoryId?.let { STUB.getRecipesByCategoryId(it).find { it.id == recipeId } }
        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}