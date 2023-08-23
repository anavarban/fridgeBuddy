package com.mready.myapplication.navigation

import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mready.myapplication.MainViewModel
import com.mready.myapplication.StartScreenState
import com.mready.myapplication.ingredientdetails.IngredientDetailsScreen
import com.mready.myapplication.ui.dashboard.DashboardScreen
import com.mready.myapplication.ui.fridge.YourFridgeScreen
import com.mready.myapplication.ui.fridge.addingredient.AddIngredientScreen
import com.mready.myapplication.ui.onboarding.LoginScreen
import com.mready.myapplication.ui.onboarding.SignUpScreen
import com.mready.myapplication.ui.onboarding.SplashScreen
import com.mready.myapplication.ui.onboarding.StartScreen
import com.mready.myapplication.ui.profile.ProfileScreen
import com.mready.myapplication.ui.recipes.RecipeScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Navigation(
    onExitFromDashboard: () -> Unit,
    notificationManager: NotificationManager,
) {
    val mainViewModel: MainViewModel = hiltViewModel()
    val navController = rememberNavController()

    LaunchedEffect(key1 = mainViewModel.startScreenFlow) {
        mainViewModel.startScreenFlow.collect {
            when (it) {
                is StartScreenState.Onboarding -> {
                    navController.navigate(Screens.StartScreen.route)
                }

                else -> {

                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screens.DashboardScreen.route
    ) {
        composable(route = Screens.SplashScreen.route) {
            SplashScreen(
                onSplashEnded = {
                    navController.navigate(Screens.StartScreen.route)
                }
            )
        }

        composable(route = Screens.StartScreen.route) {
            StartScreen(
                onClick = { navController.navigate(Screens.LoginScreen.route) },
                onExit = onExitFromDashboard
            )
        }

        composable(route = Screens.LoginScreen.route) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(Screens.DashboardScreen.route) {
                        popUpTo(Screens.LoginScreen.route) { inclusive = true }
                    }

                },
                onSignUpClick = { navController.navigate(Screens.SignUpScreen.route) }
            )
        }

        composable(route = Screens.SignUpScreen.route) {
            SignUpScreen(
                onLoginClick = { navController.navigate(Screens.LoginScreen.route) },
                onSignUpClick = {
                    navController.navigate(Screens.DashboardScreen.route) {
                        popUpTo(Screens.LoginScreen.route) { inclusive = true }
                    }

                }
            )
        }

        composable(route = Screens.DashboardScreen.route) {
            DashboardScreen(
                onSeeFridgeClick = { navController.navigate(Screens.FridgeScreen.route) },
                onRecipeClick = { navController.navigate(Screens.RecipeScreen.route + "/${it}" + "/0") },
                onProfileClick = {
                    navController.navigate(Screens.ProfileScreen.route)
                },
                onExit = onExitFromDashboard,
                onIngredientClick = { navController.navigate(Screens.DetailsScreen.route + "/${it}") },
                notificationManager = notificationManager
            )
        }

        composable(route = Screens.ProfileScreen.route) {
            ProfileScreen(
                onLogoutClick = { navController.navigate(Screens.LoginScreen.route) },
                onBackClick = { navController.navigate(Screens.DashboardScreen.route) },
            )
        }

        composable(route = Screens.RecipeScreen.route + "/{ingredients}" + "/{offset}") {
            val recipeIngredients = it.arguments?.getString("ingredients")
            val recipeOffset = it.arguments?.getString("offset")?.toInt()
            RecipeScreen(
                offset = recipeOffset ?: 0,
                ingredients = recipeIngredients ?: "",
                onBackClick = { navController.popBackStack() },
            )
        }

        composable(route = Screens.FridgeScreen.route) {
            YourFridgeScreen(
                onAddClick = { navController.navigate(Screens.AddIngredientScreen.route) },
                onBackClick = { navController.navigate(Screens.DashboardScreen.route) },
                onCardClick = { navController.navigate(Screens.DetailsScreen.route + "/${it}") },
            )

        }

        composable(route = Screens.AddIngredientScreen.route) {
            AddIngredientScreen(
                onDone = { navController.navigate(Screens.FridgeScreen.route) }
            )
        }

        composable(route = Screens.DetailsScreen.route + "/{id}") {
            val id = it.arguments?.getString("id")?.toInt() ?: 0
            IngredientDetailsScreen(
                ingredientId = id,
                onBackButtonClick = {
                    navController.popBackStack()
                },
                onRecipeClick = { ing, offset -> navController.navigate(Screens.RecipeScreen.route + "/${ing}" + "/${offset}") }
            )
        }

    }
}