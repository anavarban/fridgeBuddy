package com.mready.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mready.myapplication.ui.dashboard.DashboardScreen
import com.mready.myapplication.ui.fridge.AddAmount
import com.mready.myapplication.ui.fridge.AddExpireDate
import com.mready.myapplication.ui.fridge.AddType
import com.mready.myapplication.ui.fridge.YourFridgeScreen
import com.mready.myapplication.ui.onboarding.LoginScreen
import com.mready.myapplication.ui.onboarding.OnboardingViewModel
import com.mready.myapplication.ui.onboarding.SignUpScreen
import com.mready.myapplication.ui.onboarding.SplashScreen
import com.mready.myapplication.ui.onboarding.StartScreen
import com.mready.myapplication.ui.profile.ProfileScreen
import com.mready.myapplication.ui.recipes.RecipeScreen
import kotlinx.coroutines.delay

@Composable
fun Navigation(
    onboardingViewModel: OnboardingViewModel,
    onExitFromDashboard: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.SplashScreen.route
    ) {
        composable(route = Screens.SplashScreen.route) {
            SplashScreen(
                onSplashEnded = {
                    if (onboardingViewModel.currentUser != null) {
                        navController.navigate(Screens.DashboardScreen.route)
                    } else {
                        navController.navigate(Screens.StartScreen.route)
                    }
                }
            )
        }

        composable(route = Screens.StartScreen.route) {
            StartScreen { navController.navigate(Screens.LoginScreen.route) }
        }

        composable(route = Screens.LoginScreen.route) {
            LoginScreen(
                onboardingViewModel = onboardingViewModel,
                onLoginClick = {
                    LaunchedEffect(key1 = null) {
                        navController.navigate(Screens.DashboardScreen.route) {
                            popUpTo(Screens.LoginScreen.route) { inclusive = true }
                        }
                    }
                },
                onSignUpClick = { navController.navigate(Screens.SignUpScreen.route) }
            )
        }

        composable(route = Screens.SignUpScreen.route) {
            SignUpScreen(
                onboardingViewModel = onboardingViewModel,
                onLoginClick = { navController.navigate(Screens.LoginScreen.route) },
                //todo i think this should navigate to login????
                onSignUpClick = {
                    LaunchedEffect(key1 = null) {
                        navController.navigate(Screens.DashboardScreen.route) {
                            popUpTo(Screens.LoginScreen.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(route = Screens.DashboardScreen.route) {
            DashboardScreen(
                onboardingViewModel = onboardingViewModel,
                onSeeRecipesClick = { navController.navigate(Screens.RecipeScreen.route) },
                onSeeFridgeClick = { navController.navigate(Screens.FridgeScreen.route) },
                onIngredientEditClick = {},
                onRecipeClick = { navController.navigate(Screens.RecipeScreen.route + "/${it}") },
                onProfileClick = { navController.navigate(Screens.ProfileScreen.route) },
                onExit = onExitFromDashboard
            )
        }

        composable(route = Screens.ProfileScreen.route) {
            ProfileScreen(
                onboardingViewModel = onboardingViewModel,
                onLogoutClick = { navController.navigate(Screens.LoginScreen.route) },
            )
        }

        composable(route = Screens.RecipeScreen.route + "/{ingredients}") {
            val recipeIngredients = it.arguments?.getString("ingredients")
            RecipeScreen(ingredients = recipeIngredients ?: "")
        }

        composable(route = Screens.FridgeScreen.route) {
            YourFridgeScreen(
                onAddClick = { navController.navigate(Screens.AddType.route) },
                onBackClick = { navController.navigate(Screens.DashboardScreen.route) },
            )

        }

        composable(route = Screens.AddType.route) {
            AddType(
                onNextClick = { navController.navigate(Screens.AddAmount.route + "/${it}") },
            )
        }

        composable(route = Screens.AddAmount.route + "/{type}") {
            val type = it.arguments?.getString("type")
            AddAmount(
                ingredientName = type ?: "",
                onNextClick = { ingredientName, unit, amount ->
                    navController.navigate(Screens.AddExpireDate.route + "/${ingredientName}/${unit}/${amount}")
                },
            )
        }

        composable(route = Screens.AddExpireDate.route + "/{ingredientName}/{unit}/{amount}") {
            val ingredientName = it.arguments?.getString("ingredientName")
            val unit = it.arguments?.getString("unit")
            val amount = it.arguments?.getInt("amount")
            AddExpireDate(
                ingredientName = ingredientName ?: "",
                unit = unit ?: "",
                amount = amount ?: 0
            ) { _, _, _, _ ->
                navController.navigate(Screens.FridgeScreen.route)
            }
        }

    }
}