package com.mready.myapplication.ui.dashboard

import com.mready.myapplication.models.Ingredient
import com.mready.myapplication.models.Recipe

sealed class WidgetItemViewModel
data class RecommendedWidgetItemViewModel(private val recipes: List<Recipe>) :
    WidgetItemViewModel() {

    val displayRecipes = recipes
}

data class FridgeWidgetItemViewModel(private val ingredients: List<Ingredient>) :
    WidgetItemViewModel() {

    val soonToExpireIngredients = ingredients.sortedWith { o1, o2 ->
        if (o1.expireDate.year == o2.expireDate.year) {
            if (o1.expireDate.month == o2.expireDate.month) {
                o1.expireDate.date - o2.expireDate.date
            } else {
                o1.expireDate.month - o2.expireDate.month
            }
        } else {
            o1.expireDate.year - o2.expireDate.year
        }
    }?.map { it.name }?.take(3) ?: emptyList()

    val displayIngredients = ingredients.sortedWith { o1, o2 ->
        if (o1.expireDate.year == o2.expireDate.year) {
            if (o1.expireDate.month == o2.expireDate.month) {
                o1.expireDate.date - o2.expireDate.date
            } else {
                o1.expireDate.month - o2.expireDate.month
            }
        } else {
            o1.expireDate.year - o2.expireDate.year
        }
    }?.take(3) ?: emptyList()

}

data class PopularWidgetItemViewModel(private val recipes: List<String>) :
    WidgetItemViewModel() {

    val displayRecipes = recipes
}