package com.mready.myapplication.services

import com.mready.myapplication.api.endpoints.RecipeAPI
import com.mready.myapplication.models.Recipe
import com.mready.myapplication.models.RecipeIngredient
import com.mready.myapplication.models.RecipeInstruction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeService @Inject constructor(private val recipeAPI: RecipeAPI) {

    suspend fun getRecipe(ingredients: String): Recipe? {
        //return recipeAPI.getRecipe(ingredients)
        return Recipe(
            id = 5929,
            name = "TestTest long test name very long",
            thumbnailUrl = "https://spoonacular.com/recipeImages/716429-556x370.jpg",
            ingredients = listOf(
                RecipeIngredient(
                    ingredient = "Test",
                    position = 1
                ),
                RecipeIngredient(
                    ingredient = "Test",
                    position = 2
                ),
                RecipeIngredient(
                    ingredient = "Test",
                    position = 3
                ),
                RecipeIngredient(
                    ingredient = "Test",
                    position = 4
                ),
                RecipeIngredient(
                    ingredient = "Test",
                    position = 5
                ),
                RecipeIngredient(
                    ingredient = "Test",
                    position = 6
                ),
                RecipeIngredient(
                    ingredient = "Test",
                    position = 7
                ),
                RecipeIngredient(
                    ingredient = "Test",
                    position = 8
                ),
                RecipeIngredient(
                    ingredient = "Test",
                    position = 9
                ),
                RecipeIngredient(
                    ingredient = "Test",
                    position = 10
                ),
                RecipeIngredient(
                    ingredient = "Test",
                    position = 11
                ),
                RecipeIngredient(
                    ingredient = "Test",
                    position = 12
                ),
                RecipeIngredient(
                    ingredient = "Test",
                    position = 13
                ),
                RecipeIngredient(
                    ingredient = "Test",
                    position = 14
                ),
                RecipeIngredient(
                    ingredient = "Test",
                    position = 15
                ),
                RecipeIngredient(
                    ingredient = "Test",
                    position = 16
                ),
                RecipeIngredient(
                    ingredient = "Test",
                    position = 17
                ),
                RecipeIngredient(
                    ingredient = "Test",
                    position = 18
                ),
                RecipeIngredient(
                    ingredient = "Test",
                    position = 19
                ),
                RecipeIngredient(
                    ingredient = "Test",
                    position = 20
                ),
                RecipeIngredient(
                    ingredient = "Test",
                    position = 21
                ),
                RecipeIngredient(
                    ingredient = "Test",
                    position = 22
                ),
            ),
            instructions = listOf(
                RecipeInstruction(
                    displayText = "Preheat a nonstick electric griddle to 300°F (150°C). (Alternatively, heat a large nonstick skillet over medium-low heat.)",
                    position = 1
                ),
                RecipeInstruction(
                    displayText = "Preheat a nonstick electric griddle to 300°F (150°C). (Alternatively, heat a large nonstick skillet over medium-low heat.)",
                    position = 2
                ),
                RecipeInstruction(
                    displayText = "Preheat a nonstick electric griddle to 300°F (150°C). (Alternatively, heat a large nonstick skillet over medium-low heat.)",
                    position = 3
                ),
                RecipeInstruction(
                    displayText = "Preheat a nonstick electric griddle to 300°F (150°C). (Alternatively, heat a large nonstick skillet over medium-low heat.)",
                    position = 4
                ),
                RecipeInstruction(
                    displayText = "Preheat a nonstick electric griddle to 300°F (150°C). (Alternatively, heat a large nonstick skillet over medium-low heat.)",
                    position = 5
                ),
                RecipeInstruction(
                    displayText = "Preheat a nonstick electric griddle to 300°F (150°C). (Alternatively, heat a large nonstick skillet over medium-low heat.)",
                    position = 6
                )
            ),
            description = "Test",
            time = 100,
            yields = "1 serving",
            videoUrl = "https://www.google.com",
        )
    }

    suspend fun getRecipesByFirstExpired(firstExpired: List<String>): List<Recipe>? {
        //return recipeAPI.getRecipesByFirstExpired(firstExpired)
        val recipe = Recipe(
            id = 1,
            name = "TestTest long test name very long",
            thumbnailUrl = "https://spoonacular.com/recipeImages/716429-556x370.jpg",
            ingredients = listOf(
                RecipeIngredient(
                    ingredient = "Test",
                    position = 1
                ),
            ),
            instructions = listOf(
                RecipeInstruction(
                    displayText = "Preheat a nonstick electric griddle to 300°F (150°C). (Alternatively, heat a large nonstick skillet over medium-low heat.)",
                    position = 1
                ),
                RecipeInstruction(
                    displayText = "Preheat a nonstick electric griddle to 300°F (150°C). (Alternatively, heat a large nonstick skillet over medium-low heat.)",
                    position = 2
                ),
                RecipeInstruction(
                    displayText = "Preheat a nonstick electric griddle to 300°F (150°C). (Alternatively, heat a large nonstick skillet over medium-low heat.)",
                    position = 3
                )
            ),
            description = "Test",
            time = 100,
            yields = "1 serving",
            videoUrl = "https://www.google.com",
        )
        return listOf(recipe, recipe, recipe)
    }

}