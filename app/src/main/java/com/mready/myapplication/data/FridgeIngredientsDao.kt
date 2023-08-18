package com.mready.myapplication.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FridgeIngredientsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIngredient(ingredient: FridgeIngredients)

//    @Update
//    suspend fun updateIngredient(ingredient: FridgeIngredients)

    @Query("UPDATE fridge_contents SET user_email = :userEmail, ingredient_name = :name, quantity = :quantity, unit = :unit, exp_day = :expireDay, exp_month = :expireMonth, exp_year = :expireYear, img_url = :imgUrl WHERE id = :id")
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

    @Query("DELETE FROM fridge_contents WHERE id = :id")
    suspend fun deleteIngredient(id: Int)

    @Query("SELECT * FROM fridge_contents WHERE user_email = :email")
    fun getUserIngredients(email: String): Flow<List<FridgeIngredients>>

    @Query("SELECT * FROM fridge_contents WHERE id = :id")
    suspend fun getIngredient(id: Int): FridgeIngredients?

}

