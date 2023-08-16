package com.mready.myapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "fridge_contents"
)
data class FridgeIngredients(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "user_email")
    val userEmail: String,

    @ColumnInfo(name = "ingredient_name")
    val name: String,

    @ColumnInfo(name = "quantity")
    val quantity: Int = 0,

    @ColumnInfo(name = "unit")
    val unit: String = "",

    @ColumnInfo(name = "exp_day")
    val expireDay: Int = 0,

    @ColumnInfo(name = "exp_month")
    val expireMonth: Int = 0,

    @ColumnInfo(name = "exp_year")
    val expireYear: Int = 0,

    @ColumnInfo(name = "img_url")
    val imgUrl: String = "",


    )