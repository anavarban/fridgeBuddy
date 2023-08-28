package com.mready.myapplication.models

data class RecipeIngredient(
    val ingredient: String,
    val position: Int
)

data class RecipeInstruction(
    val displayText: String,
    val position: Int
)

data class Recipe(
    val baseIngredient: String,
    val id: Int,
    val name: String,
    val ingredients: List<RecipeIngredient>,
    val time: Int?,
    val instructions: List<RecipeInstruction>,
    val thumbnailUrl: String,
    val description: String,
    val yields: String,
    val videoUrl: String?,
)
