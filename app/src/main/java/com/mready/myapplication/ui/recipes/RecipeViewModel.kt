package com.mready.myapplication.ui.recipes

import androidx.lifecycle.ViewModel
import com.mready.myapplication.models.Recipe
import com.mready.myapplication.models.RecipeIngredient
import com.mready.myapplication.models.RecipeInstruction
import com.mready.myapplication.services.RecipeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeService: RecipeService,
) : ViewModel() {
    val uiState = MutableStateFlow<RecipeUiState>(RecipeUiState.Loading)

    fun loadRecipe(ingredients: String) {
//        viewModelScope.launch {
//            uiState.update {
//                val recipe = recipeService.getRecipe(ingredients) ?: return@update RecipeUiState.Error
//                RecipeUiState.Success(recipe)
//            }
//        }
        uiState.update {
            val recipe = Recipe(
                id = 1,
                name = "Test",
                thumbnailUrl = "https://spoonacular.com/recipeImages/716429-556x370.jpg",
                ingredients = listOf(
                    RecipeIngredient(
                        ingredient = "Test",
                        position = 1
                    ),
                ),
                instructions = listOf(
                    RecipeInstruction(
                        displayText = "Preheat a nonstick electric griddle to 300°F (150°C). (Alternatively, heat a large nonstick skillet over medium-low heat.)",
                        position = 1
                    ),
                    RecipeInstruction(
                        displayText = "Preheat a nonstick electric griddle to 300°F (150°C). (Alternatively, heat a large nonstick skillet over medium-low heat.)",
                        position = 2
                    ),
                    RecipeInstruction(
                        displayText = "Preheat a nonstick electric griddle to 300°F (150°C). (Alternatively, heat a large nonstick skillet over medium-low heat.)",
                        position = 3
                    )
                ),
                description = "Test",
                time = 100,
                yields = "1 serving",
                videoUrl = "https://www.google.com",
            )
            RecipeUiState.Success(recipe)
        }
    }

}

sealed class RecipeUiState {
    object Loading : RecipeUiState()
    data class Success(val recipe: Recipe) : RecipeUiState()
    object Error : RecipeUiState()
}