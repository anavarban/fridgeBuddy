package com.mready.myapplication.ui.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mready.myapplication.auth.AuthRepository
import com.mready.myapplication.data.FridgeIngredientsRepo
import com.mready.myapplication.models.Recipe
import com.mready.myapplication.models.RecipeIngredient
import com.mready.myapplication.models.RecipeInstruction
import com.mready.myapplication.models.toIngredient
import com.mready.myapplication.services.RecipeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DashboardState {
    object Loading : DashboardState()
    object Error : DashboardState()
    data class Success(val widgets: List<WidgetItemViewModel>) : DashboardState()
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val recipeService: RecipeService,
    private val fridgeIngredientsRepo: FridgeIngredientsRepo,
    private val authRepository: AuthRepository
) : ViewModel() {

    val currentUser = authRepository.currentUser

    private var _dashboardFlow = MutableStateFlow<DashboardState>(DashboardState.Loading)
    val dashboardFlow: StateFlow<DashboardState> = _dashboardFlow

    private val widgets = mutableListOf<WidgetItemViewModel>()

    init {
        _dashboardFlow.update { DashboardState.Loading }

    }

    fun loadDashboardWidgets() {
        widgets.clear()
        loadIngredients()
        widgets.add(
            PopularWidgetItemViewModel(
                listOf(
                    "https://www.youtube.com/watch?v=brqY65Hp15M&pp=ygUGamFtaWxh",
                    "https://www.youtube.com/watch?v=df1QU5kQMyg&pp=ygUGamFtaWxh"
                )
            )
        )
        _dashboardFlow.update {
            DashboardState.Success(widgets)
        }
        Log.d("DashboardViewModel", "loadDashboardWidgets: ${widgets.size}")

    }

    private fun loadIngredients() {
        if (currentUser != null) {
            fridgeIngredientsRepo.getUserIngredients(currentUser.email ?: "").onEach { list ->
                val ingredientsList = list.map { elem -> elem.toIngredient() }
                _dashboardFlow.update {
                    widgets.add(FridgeWidgetItemViewModel(ingredientsList))
                    DashboardState.Success(widgets)
                }

                val firstExpired = ingredientsList.map { it.name }
                loadRecipes(firstExpired)
            }.launchIn(viewModelScope)
        }
    }

    private fun loadRecipes(firstExpired: List<String>) {
//        viewModelScope.launch {
//            _recipesFlow.update {
//                DashRecipesUiState.Success(
//                    recipeService.getRecipesByFirstExpired(firstExpired)
//                )
//            }
//        }
        val recipe = Recipe(
            id = 1,
            name = "TestTest long test name very long",
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
        viewModelScope.launch {
//            delay(5000)
            _dashboardFlow.update {
                widgets.add(
                    RecommendedWidgetItemViewModel(
                        listOf(recipe, recipe, recipe),
                        firstExpired
                    )
                )
                DashboardState.Success(widgets)
            }
        }
    }

}



