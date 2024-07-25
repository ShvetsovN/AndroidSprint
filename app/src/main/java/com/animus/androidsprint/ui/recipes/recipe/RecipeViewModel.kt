package com.animus.androidsprint.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.animus.androidsprint.model.Recipe

class RecipeViewModel : ViewModel() {

    private val _recipeLiveData = MutableLiveData<RecipeState>()
    val recipeLiveData: LiveData<RecipeState> = _recipeLiveData

    private val _portionCountLiveData = MutableLiveData<Int>()
    val portionLiveData: LiveData<Int> = _portionCountLiveData

    private val _isFavoriteLiveData = MutableLiveData<Boolean>()
    val isFavoriteLiveData: LiveData<Boolean> = _isFavoriteLiveData

    init {
        Log.i("!!!", "VM created")

        _recipeLiveData.value = RecipeState()
        _isFavoriteLiveData.value = _recipeLiveData.value?.isFavorite
    }

    override fun onCleared() {
        Log.i("!!!", "VM cleared")
        super.onCleared()
    }

    data class RecipeState(
        val recipe: Recipe? = null,
        val portionCount: Int = 1,
        val isFavorite: Boolean = false,
    )
}
