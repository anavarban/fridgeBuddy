package com.mready.myapplication.api.endpoints

import com.mready.myapplication.api.FridgeBuddyApiClient
import com.mready.myapplication.models.Recipe
import com.mready.myapplication.models.RecipeIngredient
import com.mready.myapplication.models.RecipeInstruction
import com.mready.myapplication.models.RecipeNutrition
import net.mready.apiclient.get
import net.mready.json.Json
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RecipeAPI @Inject constructor(private val apiClient: FridgeBuddyApiClient) {
    suspend fun getRecipesByFirstExpired(firstExpired: List<String>): List<Recipe>? {
        val recipeList = mutableListOf<Recipe>()

        firstExpired.forEach{
            val rec = getRecipe(it, 0)
            if (rec != null) {
                recipeList.add(rec)
            }
        }

        if (recipeList.isEmpty()) return null
        return recipeList
    }

    suspend fun getRecipe(ingredients: String, offset: Int): Recipe? {
        var isSuccessful = true

        val recipe = apiClient.get(
            endpoint = "/recipes/list",
            query = mapOf("from" to offset, "size" to 1,"q" to ingredients),
            headers = mapOf("X-RapidAPI-Key" to "6c7cf6006fmshe7c316b0ab830a4p193114jsn0f66b4b0fbf9", "X-RapidAPI-Host" to "tasty.p.rapidapi.com"),
            errorHandler = {_ ->  isSuccessful = false}
        ) { json ->
            if (json["results"].array.isEmpty()) return@get null
            json["results"].array[0].toRecipe(ingredients)
        }

        if (!isSuccessful) return null
        return recipe
    }

    suspend fun getRecipesByIngredient(ingredient: String) : List<Recipe>? {

        val recipes = mutableListOf<Recipe>()
        var isSuccessful = true

        apiClient.get(
            endpoint = "/recipes/list",
            query = mapOf("from" to 0, "size" to 3,"q" to ingredient),
            headers = mapOf("X-RapidAPI-Key" to "6c7cf6006fmshe7c316b0ab830a4p193114jsn0f66b4b0fbf9", "X-RapidAPI-Host" to "tasty.p.rapidapi.com"),
            errorHandler = {_ -> isSuccessful = false}
        ) { json ->
            json["results"].array.forEach {
                recipes.add(it.toRecipe(ingredient))
            }
        }
        if (!isSuccessful) return null
        return recipes
    }
}

private fun Json.toRecipe(baseIngredient: String) = Recipe(
    id = this["id"].int,
    name = this["name"].string,
    description = this["description"].string,
    yields = this["yields"].string,
    time = this["total_time_minutes"].intOrNull,
    videoUrl = this["original_video_url"].stringOrNull,
    thumbnailUrl = this["thumbnail_url"].string,
    instructions = this["instructions"].toInstructions(),
    ingredients = this["sections"].toIngredients(),
    baseIngredient = baseIngredient,
    nutrition = if (this["nutrition"].size == 0) null else this["nutrition"].toNutrition()
)

private fun Json.toInstructions() = this.array.map {
    it.toInstruction()
}

private fun Json.toInstruction() = RecipeInstruction(
    displayText = this["display_text"].string,
    position = this["position"].int,
)

private fun Json.toIngredients() = this.array[0].toComponents()

private fun Json.toComponents() = this["components"].array.map {
    it.toIngredient()
}

private fun Json.toIngredient() = RecipeIngredient(
    ingredient = this["raw_text"].string,
    position = this["position"].int
)

private fun Json.toNutrition() = RecipeNutrition(
    calories = this["calories"].int,
    carbohydrates = this["carbohydrates"].int,
    fat = this["fat"].int,
    fiber = this["fiber"].int,
    protein = this["protein"].int,
    sugar = this["sugar"].int,
)