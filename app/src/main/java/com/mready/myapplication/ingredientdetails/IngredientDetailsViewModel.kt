package com.mready.myapplication.ingredientdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mready.myapplication.auth.AuthRepository
import com.mready.myapplication.data.FridgeIngredientsRepo
import com.mready.myapplication.models.Ingredient
import com.mready.myapplication.models.Recipe
import com.mready.myapplication.models.toFridgeIngredient
import com.mready.myapplication.models.toIngredient
import com.mready.myapplication.services.RecipeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngredientDetailsViewModel @Inject constructor(
    private val fridgeIngredientsRepo: FridgeIngredientsRepo,
    private val authRepository: AuthRepository,
    private val recipeService: RecipeService
) : ViewModel() {

    private var _ingredientDetailsFlow =
        MutableStateFlow<IngredientDetailsUiState>(IngredientDetailsUiState.Loading)
    val ingredientDetailsFlow: StateFlow<IngredientDetailsUiState> = _ingredientDetailsFlow

    private var _ingredientRecipesFlow =
        MutableStateFlow<IngredientRecipesState>(IngredientRecipesState.Loading)
    val ingredientRecipesFlow: StateFlow<IngredientRecipesState> = _ingredientRecipesFlow

    fun loadIngredientDetails(ingredientId: Int) {
        viewModelScope.launch {
            val ingredientDetails = fridgeIngredientsRepo.getIngredient(ingredientId)
            if (ingredientDetails != null) {
                _ingredientDetailsFlow.value =
                    IngredientDetailsUiState.Success(ingredientDetails.toIngredient())
            } else {
                _ingredientDetailsFlow.value = IngredientDetailsUiState.Error
            }
        }
    }

    fun deleteIngredient(ingredientId: Int) {
        viewModelScope.launch {
            fridgeIngredientsRepo.deleteIngredient(ingredientId)
        }
    }

    fun updateIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            val updatedIngredient = ingredient.toFridgeIngredient(
                authRepository.currentUser.value?.email ?: ""
            ).copy(id = ingredient.id)
            fridgeIngredientsRepo.updateIngredient(
                updatedIngredient.id,
                updatedIngredient.userEmail,
                updatedIngredient.name,
                updatedIngredient.quantity,
                updatedIngredient.unit,
                updatedIngredient.expireDay,
                updatedIngredient.expireMonth,
                updatedIngredient.expireYear,
                updatedIngredient.imgUrl
            )
            loadIngredientDetails(updatedIngredient.id)
        }
    }

    fun getRecipesByIngredient(ingredientName: String) {
        viewModelScope.launch {
            _ingredientRecipesFlow.update {
                val recipes = recipeService.getRecipesByIngredient(ingredientName)
                    ?: return@update IngredientRecipesState.Error

                IngredientRecipesState.Success(
                    recipes
                )
            }
        }
    }


}

sealed class IngredientDetailsUiState {
    data class Success(var ingredient: Ingredient) : IngredientDetailsUiState()
    object Error : IngredientDetailsUiState()
    object Loading : IngredientDetailsUiState()
}

sealed class IngredientRecipesState {
    data class Success(var recipes: List<Recipe>) : IngredientRecipesState()
    object Error : IngredientRecipesState()
    object Loading : IngredientRecipesState()
}