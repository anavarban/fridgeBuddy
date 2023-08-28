package com.mready.myapplication.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mready.myapplication.auth.AuthRepository
import com.mready.myapplication.data.FridgeIngredientsRepo
import com.mready.myapplication.models.toIngredient
import com.mready.myapplication.services.RecipeService
import com.mready.myapplication.ui.utils.getFirstThreeDistinct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
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

    fun loadDashboardWidgets() {
        val currentUser = authRepository.currentUser.value
            ?: return _dashboardFlow.update { DashboardState.Success(emptyList()) }

        _dashboardFlow.update { DashboardState.Loading }
        fridgeIngredientsRepo.getUserIngredients(currentUser.email ?: "").onEach { list ->
            val widgets = mutableListOf<WidgetItemViewModel>()

            val ingredientsList = list.map { elem -> elem.toIngredient() }
            val fridgeWidgetItem = FridgeWidgetItemViewModel(ingredientsList)
            widgets.add(fridgeWidgetItem)

            val firstExpired = fridgeWidgetItem.displayIngredients.map { it.name }
            val recipes = recipeService.getRecipesByFirstExpired(firstExpired.distinct())
            widgets.add(RecommendedWidgetItemViewModel(recipes ?: emptyList(), firstExpired))

            widgets.add(
                PopularWidgetItemViewModel(
                    listOf(
                        "brqY65Hp15M",
                        "df1QU5kQMyg"
                    )
                )
            )
            _dashboardFlow.update {
                DashboardState.Success(widgets)
            }
        }.launchIn(viewModelScope)
    }

}