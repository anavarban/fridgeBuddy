package com.mready.myapplication.ui.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mready.myapplication.auth.AuthRepository
import com.mready.myapplication.data.FridgeIngredientsRepo
import com.mready.myapplication.models.toIngredient
import com.mready.myapplication.services.RecipeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
        if (currentUser != null) {
            fridgeIngredientsRepo.getUserIngredients(currentUser.email ?: "").onEach { list ->
                val ingredientsList = list.map { elem -> elem.toIngredient() }
                widgets.add(FridgeWidgetItemViewModel(ingredientsList))

                val firstExpired = ingredientsList.map { it.name }
                val recipes = recipeService.getRecipesByFirstExpired(firstExpired)
                widgets.add(RecommendedWidgetItemViewModel(recipes ?: emptyList(), firstExpired))

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
            }.launchIn(viewModelScope)
        }
    }

}