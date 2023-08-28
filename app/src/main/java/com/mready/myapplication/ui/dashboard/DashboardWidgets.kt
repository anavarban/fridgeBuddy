package com.mready.myapplication.ui.dashboard

import com.mready.myapplication.models.Ingredient
import com.mready.myapplication.models.Recipe
import com.mready.myapplication.ui.utils.getFirstThreeDistinct
import java.util.Calendar

sealed class WidgetItemViewModel
data class RecommendedWidgetItemViewModel(
    private val recipes: List<Recipe>,
    private val ingredients: List<String>
) :
    WidgetItemViewModel() {
    val displayRecipes = recipes.distinct()
    val soonToExpireIngredients = ingredients
}

data class FridgeWidgetItemViewModel(private val ingredients: List<Ingredient>) :
    WidgetItemViewModel() {

    val date: Calendar = Calendar.getInstance()
    private val formattedDate = com.mready.myapplication.models.Date(
        year = date.get(Calendar.YEAR),
        month = date.get(Calendar.MONTH) + 1,
        date = date.get(Calendar.DAY_OF_MONTH)
    )

    val displayIngredients = ingredients
        .sortedWith { o1, o2 ->
        if (o1.expireDate.year == o2.expireDate.year) {
            if (o1.expireDate.month == o2.expireDate.month) {
                o1.expireDate.date - o2.expireDate.date
            } else {
                o1.expireDate.month - o2.expireDate.month
            }
        } else {
            o1.expireDate.year - o2.expireDate.year
        }
    }.getFirstThreeDistinct()

}

data class PopularWidgetItemViewModel(private val recipes: List<String>) :
    WidgetItemViewModel() {
    val displayRecipes = recipes
}