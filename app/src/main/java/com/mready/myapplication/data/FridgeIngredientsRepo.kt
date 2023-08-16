package com.mready.myapplication.data

import com.mready.myapplication.models.Ingredient
import kotlinx.coroutines.flow.Flow

interface FridgeIngredientsRepo {

    fun getUserIngredients(email: String): Flow<List<FridgeIngredients>>

    suspend fun insertIngredient(ingredient: FridgeIngredients)

    suspend fun updateIngredient(ingredient: FridgeIngredients)

    suspend fun deleteIngredient(ingredient: FridgeIngredients)

}