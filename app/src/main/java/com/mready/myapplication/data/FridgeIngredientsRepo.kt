package com.mready.myapplication.data

import com.mready.myapplication.models.Ingredient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface FridgeIngredientsRepo {
    fun getUserIngredients(email: String): Flow<List<FridgeIngredients>>

    suspend fun insertIngredient(ingredient: FridgeIngredients)

    suspend fun updateIngredient(
        id: Int,
        userEmail: String,
        name: String,
        quantity: Int,
        unit: String,
        expireDay: Int,
        expireMonth: Int,
        expireYear: Int,
        imgUrl: String
    )

    suspend fun deleteIngredient(id: Int)

    suspend fun getIngredient(id: Int): FridgeIngredients?

}