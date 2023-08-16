package com.mready.myapplication.ui.fridge

import androidx.lifecycle.ViewModel
import com.mready.myapplication.data.FridgeIngredients
import com.mready.myapplication.data.FridgeIngredientsRepo
import com.mready.myapplication.models.Ingredient
import com.mready.myapplication.models.toIngredient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.mready.myapplication.models.toFridgeIngredient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class FridgeViewModel @Inject constructor(private val fridgeIngredientsRepo: FridgeIngredientsRepo): ViewModel() {

    private var _fridgeFlow = MutableStateFlow<FridgeIngredientsUiState>(FridgeIngredientsUiState.Loading)
    val fridgeFlow = _fridgeFlow

    init {
        _fridgeFlow.update { FridgeIngredientsUiState.Loading }
    }

    fun loadIngredients(email: String) {
        fridgeIngredientsRepo.getUserIngredients(email).onEach {list ->
            _fridgeFlow.update {
                FridgeIngredientsUiState.Success(list.map { it.toIngredient() })
            }
        }.launchIn(viewModelScope)
    }

    fun insertIngredient(ingredient: Ingredient, user: String) {
        viewModelScope.launch {
            fridgeIngredientsRepo.insertIngredient(ingredient.toFridgeIngredient(user))
        }
        loadIngredients(user)
    }

    //todo edit, delete

    fun deleteIngredient(ingredient: Ingredient, user: String) {
        viewModelScope.launch {
            fridgeIngredientsRepo.deleteIngredient(ingredient.toFridgeIngredient(user))
        }
        loadIngredients(user)
    }

    fun editIngredient(ingredient: Ingredient, user: String) {
        viewModelScope.launch {
            fridgeIngredientsRepo.updateIngredient(ingredient.toFridgeIngredient(user))
        }
        loadIngredients(user)
    }

}

sealed class FridgeIngredientsUiState {
    object Loading : FridgeIngredientsUiState()
    data class Success(val ingredients: List<Ingredient>?) : FridgeIngredientsUiState()
    object Error : FridgeIngredientsUiState()
}
