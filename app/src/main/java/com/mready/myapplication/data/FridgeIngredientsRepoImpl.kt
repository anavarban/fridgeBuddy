package com.mready.myapplication.data

class FridgeIngredientsRepoImpl (private val ingredientsDao: FridgeIngredientsDao): FridgeIngredientsRepo {

    override fun getUserIngredients(email: String) = ingredientsDao.getUserIngredients(email)
    override suspend fun insertIngredient(ingredient: FridgeIngredients) = ingredientsDao.insertIngredient(ingredient)
    override suspend fun updateIngredient(
        id: Int,
        userEmail: String,
        name: String,
        quantity: Int,
        unit: String,
        expireDay: Int,
        expireMonth: Int,
        expireYear: Int,
        imgUrl: String
    ) = ingredientsDao.updateIngredient(
        id,
        userEmail,
        name,
        quantity,
        unit,
        expireDay,
        expireMonth,
        expireYear,
        imgUrl
    )
    override suspend fun deleteIngredient(id: Int) {
        ingredientsDao.deleteIngredient(id)
    }
    override suspend fun getIngredient(id: Int): FridgeIngredients? = ingredientsDao.getIngredient(id)


}