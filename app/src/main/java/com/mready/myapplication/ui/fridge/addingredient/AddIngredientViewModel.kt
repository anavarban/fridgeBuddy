package com.mready.myapplication.ui.fridge.addingredient

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mready.myapplication.auth.AuthRepository
import com.mready.myapplication.data.FridgeIngredients
import com.mready.myapplication.data.FridgeIngredientsRepo
import com.mready.myapplication.models.Date
import com.mready.myapplication.models.Ingredient
import com.mready.myapplication.ui.utils.ingredientToUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AddIngredientViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val fridgeIngredientsRepo: FridgeIngredientsRepo,
) : ViewModel() {

    val currentUser = authRepository.currentUser

    private var _addIngredientUiState =
        MutableStateFlow<AddIngredientUiState>(AddIngredientUiState.Loading)
    val addIngredientUiState = _addIngredientUiState

    var type: String = ""
    var amount: Int = 0
    var unit: String = ""
    var date: Long = 0

    init {
        _addIngredientUiState.value = AddIngredientUiState.Loading
    }

    fun addIngredient() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = date
            }
            val ingredient = FridgeIngredients(
                userEmail = currentUser?.email ?: "",
                name = type,
                unit = unit,
                quantity = amount,
                expireYear = calendar.get(Calendar.YEAR),
                expireMonth = calendar.get(Calendar.MONTH) + 1,
                expireDay = calendar.get(Calendar.DAY_OF_MONTH),
                imgUrl = ingredientToUrl[type] ?: "",
            )
            fridgeIngredientsRepo.insertIngredient(ingredient)
            _addIngredientUiState.update {
                AddIngredientUiState.Success(type, amount, unit, date)
            }
        }
    }

}

sealed class AddIngredientUiState {
    object Loading : AddIngredientUiState()
    data class Success(val type: String, val amount: Int, val unit: String, val date: Long) :
        AddIngredientUiState()

    data class Error(val message: String) : AddIngredientUiState()
}