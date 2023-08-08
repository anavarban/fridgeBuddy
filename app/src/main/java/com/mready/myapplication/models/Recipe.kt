package com.mready.myapplication.models

data class RecipeIngredient(
    val ingredient: String,
    val position: Int
    // TODO maybe we need the measuring unit?
)

data class RecipeInstruction(
    val displayText: String,
    val position: Int
)

data class Recipe(
    val id: Int,
    val name: String,
    val ingredients: List<RecipeIngredient>,
    val time: Int?,
    val instructions: List<RecipeInstruction>,
    val thumbnailUrl: String,
    val description: String,
    val yields: String,
    val videoUrl: String,
    //val kcal
)
//
//val mockRecipes = listOf(
//    Recipe(
//        "Recipe 1", listOf(RecipeIngredient("Flour", 1.0), RecipeIngredient("Water", 1.0)),
//        2.0, listOf("do step 1", "do step 2", "do step 3"), "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8Zm9vZHxlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=500&q=60"
//    ),
//    Recipe(
//        "Recipe 2", listOf(RecipeIngredient("Flour", 2.0), RecipeIngredient("Water", 2.0)),
//        2.0, listOf("do step 1", "do step 2", "do step 3"), "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8Zm9vZHxlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=500&q=60"
//    ),
//    Recipe(
//        "Recipe 3", listOf(RecipeIngredient("Flour", 3.0), RecipeIngredient("Water", 4.0)),
//        2.0, listOf("do step 1", "do step 2", "do step 3"), "https://images.unsplash.com/photo-1565958011703-44f9829ba187?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OXx8Zm9vZHxlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=500&q=60"
//    )
//)