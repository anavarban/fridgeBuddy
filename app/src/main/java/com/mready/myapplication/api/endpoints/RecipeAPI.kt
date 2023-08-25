package com.mready.myapplication.api.endpoints

import javax.inject.Inject
import javax.inject.Singleton
import com.mready.myapplication.api.FridgeBuddyApiClient
import com.mready.myapplication.models.Recipe
import com.mready.myapplication.models.RecipeIngredient
import com.mready.myapplication.models.RecipeInstruction
import net.mready.apiclient.get
import net.mready.json.Json


@Singleton
class RecipeAPI @Inject constructor(private val apiClient: FridgeBuddyApiClient) {
//    suspend fun getRecipesByFirstExpired(firstExpired: List<String>): List<Recipe>? {
//        var recipeList = mutableListOf<Recipe>()
//
//        firstExpired.forEach{
//            val rec = getRecipe(it, 0)
//            if (rec != null) {
//                recipeList.add(rec)
//            }
//        }
//
//        if (recipeList.isEmpty()) return null
//        return recipeList
//    }
//
//    suspend fun getRecipe(ingredients: String, offset: Int): Recipe? {
//        var isSuccessful = true
//
//        val recipe = apiClient.get(
//            endpoint = "/recipes/list",
//            query = mapOf("from" to offset, "size" to 1,"q" to ingredients),
//            headers = mapOf("X-RapidAPI-Key" to "3f4d1155c6msh66d70d2a24e50a9p1dc35fjsne99d6ec86d23", "X-RapidAPI-Host" to "tasty.p.rapidapi.com"),
//            errorHandler = {_ ->  isSuccessful = false}
//        ) { json ->
//            json["results"].array[0].toRecipe()
//        }
//
//        if (!isSuccessful) return null
//        return recipe
//    }
//
//    suspend fun getRecipesByIngredient(ingredient: String) : List<Recipe>? {
//
//        val recipes = mutableListOf<Recipe>()
//        var isSuccessful = true
//
//        apiClient.get(
//            endpoint = "/recipes/list",
//            query = mapOf("from" to 0, "size" to 3,"q" to ingredient),
//            headers = mapOf("X-RapidAPI-Key" to "3f4d1155c6msh66d70d2a24e50a9p1dc35fjsne99d6ec86d23", "X-RapidAPI-Host" to "tasty.p.rapidapi.com"),
//            errorHandler = {_ -> isSuccessful = false}
//        ) { json ->
//            json["results"].array.forEach {
//                recipes.add(it.toRecipe())
//            }
//        }
//        if (!isSuccessful) return null
//        return recipes
//    }
}

private fun Json.toRecipe() = Recipe(
    id = this["id"].int,
    name = this["name"].string,
    description = this["description"].string,
    yields = this["yields"].string,
    time = this["total_time_minutes"].intOrNull,
    videoUrl = this["original_video_url"].stringOrNull,
    thumbnailUrl = this["thumbnail_url"].string,
    instructions = this["instructions"].toInstructions(),
    ingredients = this["sections"].toIngredients()
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