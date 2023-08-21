package com.mready.myapplication.ingredientdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mready.myapplication.auth.AuthRepository
import com.mready.myapplication.data.FridgeIngredientsRepo
import com.mready.myapplication.models.Ingredient
import com.mready.myapplication.models.toFridgeIngredient
import com.mready.myapplication.models.toIngredient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngredientDetailsViewModel @Inject constructor(
    private val fridgeIngredientsRepo: FridgeIngredientsRepo,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _ingredientDetailsFlow =
        MutableStateFlow<IngredientDetailsUiState>(IngredientDetailsUiState.Loading)
    val ingredientDetailsFlow: StateFlow<IngredientDetailsUiState> = _ingredientDetailsFlow

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


}

sealed class IngredientDetailsUiState {
    data class Success(var ingredient: Ingredient) : IngredientDetailsUiState()
    object Error : IngredientDetailsUiState()
    object Loading : IngredientDetailsUiState()
}