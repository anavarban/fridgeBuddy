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

    @Update
    suspend fun updateIngredient(ingredient: FridgeIngredients)

    @Query("DELETE FROM fridge_contents WHERE id = :id")
    suspend fun deleteIngredient(id: Int)

    @Query("SELECT * FROM fridge_contents WHERE user_email = :email")
    fun getUserIngredients(email: String): Flow<List<FridgeIngredients>>

}

