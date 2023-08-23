package com.mready.myapplication.services

import com.mready.myapplication.api.endpoints.RecipeAPI
import com.mready.myapplication.models.Recipe
import com.mready.myapplication.models.RecipeIngredient
import com.mready.myapplication.models.RecipeInstruction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeService @Inject constructor(private val recipeAPI: RecipeAPI) {

    suspend fun getRecipe(ingredients: String, offset: Int): Recipe? {
//        return recipeAPI.getRecipe(ingredients, offset)
        return Recipe(
            id = 5929,
            name = "Low-Carb Avocado Chicken Salad",
            thumbnailUrl = "https://img.buzzfeed.com/thumbnailer-prod-us-east-1/45b4efeb5d2c4d29970344ae165615ab/FixedFBFinal.jpg",
            ingredients = listOf(
                RecipeIngredient(
                    ingredient = "⅔ cup plain Greek yogurt",
                    position = 1
                ),
                RecipeIngredient(
                    ingredient = "1 tablespoon lime juice",
                    position = 2
                ),
                RecipeIngredient(
                    ingredient = "Pepper, to taste",
                    position = 3
                ),
                RecipeIngredient(
                    ingredient = "⅛ teaspoon chili powder",
                    position = 4
                ),
                RecipeIngredient(
                    ingredient = "1 avocado, cubed, divided",
                    position = 5
                ),
                RecipeIngredient(
                    ingredient = "2 chicken breasts, cooked and shredded",
                    position = 6
                ),
                RecipeIngredient(
                    ingredient = "1 celery stalk, diced",
                    position = 7
                ),
                RecipeIngredient(
                    ingredient = "2 tablespoons diced red onion",
                    position = 8
                ),
                RecipeIngredient(
                    ingredient = "Kosher salt, to taste",
                    position = 9
                ),
                RecipeIngredient(
                    ingredient = "Low-carb bread, for serving, optional",
                    position = 10
                ),
                RecipeIngredient(
                    ingredient = "Fresh cilantro leaves, for garnish",
                    position = 11
                ),
            ),
            instructions = listOf(
                RecipeInstruction(
                    displayText = "In a blender or food processor, combine the yogurt, lime juice, pepper, and chili powder and pulse to combine. Add ½ of the avocado and blend until creamy.",
                    position = 1
                ),
                RecipeInstruction(
                    displayText = "In a medium bowl, combine the chicken, yogurt sauce, celery, the remaining ½ avocado, onion, and salt. Mix until well combined.",
                    position = 2
                ),
                RecipeInstruction(
                    displayText = "Serve on low-carb bread and garnish with cilantro, or as desired.",
                    position = 3
                ),
                RecipeInstruction(
                    displayText = "Enjoy!",
                    position = 4
                ),
            ),
            description = "This chicken salad is a lunchtime delight! Packed with creamy avocado, tender chicken, and crunchy veggies, it's a healthy and satisfying meal that won't weigh you down. Tossed in a tangy yogurt dressing with a hint of spice, it's a flavor explosion that's perfect for a light meal.",
            time = 20,
            yields = "Servings: 4",
            videoUrl = "https://s3.amazonaws.com/video-api-prod/assets/a0e1b07dc71c4ac6b378f24493ae2d85/FixedFBFinal.mp4",
        )
    }

    suspend fun getRecipesByFirstExpired(firstExpired: List<String>): List<Recipe>? {
//        return recipeAPI.getRecipesByFirstExpired(firstExpired)
        val recipe = Recipe(
            id = 5929,
            name = "Low-Carb Avocado Chicken Salad",
            thumbnailUrl = "https://img.buzzfeed.com/thumbnailer-prod-us-east-1/45b4efeb5d2c4d29970344ae165615ab/FixedFBFinal.jpg",
            ingredients = listOf(
                RecipeIngredient(
                    ingredient = "⅔ cup plain Greek yogurt",
                    position = 1
                ),
                RecipeIngredient(
                    ingredient = "1 tablespoon lime juice",
                    position = 2
                ),
                RecipeIngredient(
                    ingredient = "Pepper, to taste",
                    position = 3
                ),
                RecipeIngredient(
                    ingredient = "⅛ teaspoon chili powder",
                    position = 4
                ),
                RecipeIngredient(
                    ingredient = "1 avocado, cubed, divided",
                    position = 5
                ),
                RecipeIngredient(
                    ingredient = "2 chicken breasts, cooked and shredded",
                    position = 6
                ),
                RecipeIngredient(
                    ingredient = "1 celery stalk, diced",
                    position = 7
                ),
                RecipeIngredient(
                    ingredient = "2 tablespoons diced red onion",
                    position = 8
                ),
                RecipeIngredient(
                    ingredient = "Kosher salt, to taste",
                    position = 9
                ),
                RecipeIngredient(
                    ingredient = "Low-carb bread, for serving, optional",
                    position = 10
                ),
                RecipeIngredient(
                    ingredient = "Fresh cilantro leaves, for garnish",
                    position = 11
                ),
            ),
            instructions = listOf(
                RecipeInstruction(
                    displayText = "In a blender or food processor, combine the yogurt, lime juice, pepper, and chili powder and pulse to combine. Add ½ of the avocado and blend until creamy.",
                    position = 1
                ),
                RecipeInstruction(
                    displayText = "In a medium bowl, combine the chicken, yogurt sauce, celery, the remaining ½ avocado, onion, and salt. Mix until well combined.",
                    position = 2
                ),
                RecipeInstruction(
                    displayText = "Serve on low-carb bread and garnish with cilantro, or as desired.",
                    position = 3
                ),
                RecipeInstruction(
                    displayText = "Enjoy!",
                    position = 4
                ),
            ),
            description = "This chicken salad is a lunchtime delight! Packed with creamy avocado, tender chicken, and crunchy veggies, it's a healthy and satisfying meal that won't weigh you down. Tossed in a tangy yogurt dressing with a hint of spice, it's a flavor explosion that's perfect for a light meal.",
            time = 20,
            yields = "Servings: 4",
            videoUrl = "https://s3.amazonaws.com/video-api-prod/assets/a0e1b07dc71c4ac6b378f24493ae2d85/FixedFBFinal.mp4",
        )
        return listOf(recipe, recipe, recipe)
    }

    suspend fun getRecipesByIngredient(ingredient: String): List<Recipe> {
//        return recipeAPI.getRecipesByIngredient(ingredient)
        val recipe = Recipe(
            id = 5929,
            name = "Low-Carb Avocado Chicken Salad",
            thumbnailUrl = "https://img.buzzfeed.com/thumbnailer-prod-us-east-1/45b4efeb5d2c4d29970344ae165615ab/FixedFBFinal.jpg",
            ingredients = listOf(
                RecipeIngredient(
                    ingredient = "⅔ cup plain Greek yogurt",
                    position = 1
                ),
                RecipeIngredient(
                    ingredient = "1 tablespoon lime juice",
                    position = 2
                ),
                RecipeIngredient(
                    ingredient = "Pepper, to taste",
                    position = 3
                ),
                RecipeIngredient(
                    ingredient = "⅛ teaspoon chili powder",
                    position = 4
                ),
                RecipeIngredient(
                    ingredient = "1 avocado, cubed, divided",
                    position = 5
                ),
                RecipeIngredient(
                    ingredient = "2 chicken breasts, cooked and shredded",
                    position = 6
                ),
                RecipeIngredient(
                    ingredient = "1 celery stalk, diced",
                    position = 7
                ),
                RecipeIngredient(
                    ingredient = "2 tablespoons diced red onion",
                    position = 8
                ),
                RecipeIngredient(
                    ingredient = "Kosher salt, to taste",
                    position = 9
                ),
                RecipeIngredient(
                    ingredient = "Low-carb bread, for serving, optional",
                    position = 10
                ),
                RecipeIngredient(
                    ingredient = "Fresh cilantro leaves, for garnish",
                    position = 11
                ),
            ),
            instructions = listOf(
                RecipeInstruction(
                    displayText = "In a blender or food processor, combine the yogurt, lime juice, pepper, and chili powder and pulse to combine. Add ½ of the avocado and blend until creamy.",
                    position = 1
                ),
                RecipeInstruction(
                    displayText = "In a medium bowl, combine the chicken, yogurt sauce, celery, the remaining ½ avocado, onion, and salt. Mix until well combined.",
                    position = 2
                ),
                RecipeInstruction(
                    displayText = "Serve on low-carb bread and garnish with cilantro, or as desired.",
                    position = 3
                ),
                RecipeInstruction(
                    displayText = "Enjoy!",
                    position = 4
                ),
            ),
            description = "This chicken salad is a lunchtime delight! Packed with creamy avocado, tender chicken, and crunchy veggies, it's a healthy and satisfying meal that won't weigh you down. Tossed in a tangy yogurt dressing with a hint of spice, it's a flavor explosion that's perfect for a light meal.",
            time = 20,
            yields = "Servings: 4",
            videoUrl = "https://s3.amazonaws.com/video-api-prod/assets/a0e1b07dc71c4ac6b378f24493ae2d85/FixedFBFinal.mp4",
        )
        return listOf(recipe, recipe, recipe)
    }

}