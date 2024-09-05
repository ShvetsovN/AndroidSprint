package com.animus.androidsprint.ui.categories

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.animus.androidsprint.Constants
import com.animus.androidsprint.R
import com.animus.androidsprint.databinding.FragmentListCategoriesBinding
import com.animus.androidsprint.model.Category
import com.animus.androidsprint.ui.recipes.recipeList.RecipeListFragment

class CategoriesListFragment : Fragment() {

    private val viewModel: CategoriesListViewModel by viewModels()
    private val categoriesListAdapter = CategoriesListAdapter()
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListCategoriesBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        val view = binding.root
        Log.e("CategoriesListFragment", "create")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivFragmentListCategoriesHeader.setImageResource(R.drawable.bcg_categories)
        initRecycle()
    }

    override fun onDestroyView() {
        Log.e("CategoriesListFragment", "destroy")
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycle() {
        val recyclerView: RecyclerView = binding.rvCategories
        recyclerView.adapter = categoriesListAdapter
        viewModel.categoriesListLiveData.observe(viewLifecycleOwner) { categoryState ->
            categoriesListAdapter.dataSet = categoryState.categories
            categoriesListAdapter.setOnItemClickListener(object :
                CategoriesListAdapter.OnItemClickListener {
                 override fun onItemClick(category: Category) {
                     openRecipesByCategoryId(category)
                }
            })
        }

    }

    private fun openRecipesByCategoryId(category: Category) {
        val bundle = Bundle().apply {
            category.id.let {
                putInt(Constants.ARG_CATEGORY_ID, it)
                putString(Constants.ARG_CATEGORY_NAME, category.title)
                putString(Constants.ARG_CATEGORY_IMAGE_URL, category.imageUrl)
            }
        }
        parentFragmentManager.commit {
            replace<RecipeListFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}