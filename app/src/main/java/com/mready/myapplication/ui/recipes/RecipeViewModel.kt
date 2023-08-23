package com.mready.myapplication.ui.recipes

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mready.myapplication.models.Recipe
import com.mready.myapplication.models.RecipeIngredient
import com.mready.myapplication.models.RecipeInstruction
import com.mready.myapplication.services.RecipeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeService: RecipeService,
) : ViewModel() {
    val uiState = MutableStateFlow<RecipeUiState>(RecipeUiState.Loading)

    fun loadRecipe(ingredients: String, offset: Int) {
        viewModelScope.launch {
            uiState.update {
                val recipe = recipeService.getRecipe(ingredients, offset) ?: return@update RecipeUiState.Error
                RecipeUiState.Success(recipe)
            }
        }
    }

}

sealed class RecipeUiState {
    object Loading : RecipeUiState()
    data class Success(val recipe: Recipe) : RecipeUiState()
    object Error : RecipeUiState()
}