package com.mready.myapplication.models


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
    val image: String
)

val mockedIngredients = listOf(
    Ingredient(1, "Flour", Date(year = 2023, month = 12, date = 12), 7, "https://images.unsplash.com/photo-1610725664285-7c57e6eeac3f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8ZmxvdXJ8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60"),
    Ingredient(2, "Water", Date(year = 2023, month = 12, date = 12), 7, "https://images.unsplash.com/photo-1553564552-02656d6a2390?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8d2F0ZXIlMjBib3R0bGV8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60"),
    Ingredient(2, "Milk", Date(year = 2023, month = 12, date = 12), 7, "https://plus.unsplash.com/premium_photo-1664647903517-70bb8213c743?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8bWlsa3xlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=500&q=60"),
    Ingredient(1, "Flour", Date(year = 2023, month = 12, date = 12), 7, "https://images.unsplash.com/photo-1610725664285-7c57e6eeac3f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8ZmxvdXJ8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60"),
    Ingredient(2, "Water", Date(year = 2023, month = 12, date = 12), 7, "https://images.unsplash.com/photo-1553564552-02656d6a2390?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8d2F0ZXIlMjBib3R0bGV8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60"),
    Ingredient(2, "Milk", Date(year = 2023, month = 12, date = 12), 7, "https://plus.unsplash.com/premium_photo-1664647903517-70bb8213c743?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8bWlsa3xlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=500&q=60"),
    Ingredient(1, "Flour", Date(year = 2023, month = 12, date = 12), 7, "https://images.unsplash.com/photo-1610725664285-7c57e6eeac3f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8ZmxvdXJ8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60"),
    Ingredient(2, "Water", Date(year = 2023, month = 12, date = 12), 7, "https://images.unsplash.com/photo-1553564552-02656d6a2390?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8d2F0ZXIlMjBib3R0bGV8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60"),
    Ingredient(2, "Milk", Date(year = 2023, month = 12, date = 12), 7, "https://plus.unsplash.com/premium_photo-1664647903517-70bb8213c743?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8bWlsa3xlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=500&q=60"),
)