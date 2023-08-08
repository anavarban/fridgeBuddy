package com.mready.myapplication.services

import com.mready.myapplication.api.endpoints.RecipeAPI
import com.mready.myapplication.models.Recipe
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeService @Inject constructor(private val recipeAPI: RecipeAPI) {

    suspend fun getRecipe(ingredients: String): Recipe? {
        return recipeAPI.getRecipe(ingredients)
    }

}