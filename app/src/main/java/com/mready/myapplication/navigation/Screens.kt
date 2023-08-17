package com.mready.myapplication.navigation

sealed class Screens (val route: String) {
    object SplashScreen: Screens("splash_screen")
    object StartScreen: Screens("start_screen")
    object LoginScreen: Screens("login_screen")
    object SignUpScreen: Screens("sign_up_screen")
    object DashboardScreen: Screens("dashboard_screen")
    object RecipeScreen: Screens("recipe_screen")
    object FridgeScreen: Screens("fridge_screen")
    object AddType: Screens("add_type")
    object AddAmount: Screens("add_amount")
    object AddExpireDate: Screens("add_expire_date")

    object AddIngredientScreen: Screens("add_ingredient_screen")
    object ProfileScreen: Screens("profile_screen")
}
