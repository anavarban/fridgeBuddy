package com.mready.myapplication.models

import com.mready.myapplication.data.FridgeIngredients


data class Date (
    val year: Int,
    val month: Int,
    val date: Int
)
data class Ingredient (
    val id: Int,
    val name: String,
    val expireDate: Date,
    val quantity: Int,
    val image: String,
    val unit: String = "g"
)

fun FridgeIngredients.toIngredient() = Ingredient(
    id = this.id,
    name = this.name,
    expireDate = Date(
        year = this.expireYear,
        month = this.expireMonth,
        date = this.expireDay
    ),
    quantity = this.quantity,
    image = this.imgUrl,
    unit = this.unit
)

fun Ingredient.toFridgeIngredient(user: String) = FridgeIngredients(
    name = this.name,
    expireYear = this.expireDate.year,
    expireMonth = this.expireDate.month,
    expireDay = this.expireDate.date,
    quantity = this.quantity,
    imgUrl = this.image,
    unit = this.unit,
    userEmail = user
)

