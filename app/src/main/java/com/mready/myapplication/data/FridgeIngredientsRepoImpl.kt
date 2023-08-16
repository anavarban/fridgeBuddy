package com.mready.myapplication.data

class FridgeIngredientsRepoImpl (private val ingredientsDao: FridgeIngredientsDao): FridgeIngredientsRepo {

    override fun getUserIngredients(email: String) = ingredientsDao.getUserIngredients(email)
    override suspend fun insertIngredient(ingredient: FridgeIngredients) = ingredientsDao.insertIngredient(ingredient)
    override suspend fun updateIngredient(ingredient: FridgeIngredients) = ingredientsDao.updateIngredient(ingredient)
    override suspend fun deleteIngredient(ingredient: FridgeIngredients) = ingredientsDao.deleteIngredient(ingredient)

}