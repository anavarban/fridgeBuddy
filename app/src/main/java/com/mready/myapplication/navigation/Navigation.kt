package com.mready.myapplication.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mready.myapplication.MainViewModel
import com.mready.myapplication.StartScreenState
import com.mready.myapplication.ui.dashboard.DashboardScreen
import com.mready.myapplication.ui.fridge.YourFridgeScreen
import com.mready.myapplication.ui.fridge.addingredient.AddIngredientScreen
import com.mready.myapplication.ui.fridge.scan.ScanResultScreen
import com.mready.myapplication.ui.fridge.scan.ScanScreen
import com.mready.myapplication.ui.fridge.scan.ScanViewModel
import com.mready.myapplication.ui.ingredientdetails.IngredientDetailsScreen
import com.mready.myapplication.ui.onboarding.StartScreen
import com.mready.myapplication.ui.onboarding.forgotpass.ForgotPassScreen
import com.mready.myapplication.ui.onboarding.login.LoginScreen
import com.mready.myapplication.ui.onboarding.signup.SignUpScreen
import com.mready.myapplication.ui.profile.ProfileScreen
import com.mready.myapplication.ui.recipes.RecipeScreen
import com.mready.myapplication.ui.utils.DisplayError
import com.mready.myapplication.ui.utils.NetworkStatus

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Navigation(
    onExitFromDashboard: () -> Unit,
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
                    //do nothing
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screens.DashboardScreen.route
    ) {
//        composable(route = Screens.SplashScreen.route) {
//            SplashScreen(
//                onSplashEnded = {
//                    navController.navigate(Screens.StartScreen.route)
//                }
//            )
//        }

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
                onSignUpClick = { navController.navigate(Screens.SignUpScreen.route) },
                onForgotClick = { navController.navigate(Screens.ForgotPassScreen.route + "?email=$it") }
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

        composable(
            route = Screens.ForgotPassScreen.route + "?email={email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType; nullable = true })
        ) {
            val email = it.arguments?.getString("email") ?: ""
            NetworkStatus(
                onNetworkAvailable = {
                    ForgotPassScreen(
                        onBackClick = { navController.popBackStack() },
                        onSendClick = { navController.navigate(Screens.LoginScreen.route) },
                        email = email
                    )
                }
            ) {
                DisplayError()
            }
        }

        composable(route = Screens.DashboardScreen.route) {
            NetworkStatus(
                onNetworkAvailable = {
                    DashboardScreen(
                        onSeeFridgeClick = { navController.navigate(Screens.FridgeScreen.route) },
                        onRecipeClick = { navController.navigate(Screens.RecipeScreen.route + "/${it}" + "/0") },
                        onProfileClick = {
                            navController.navigate(Screens.ProfileScreen.route)
                        },
                        onExit = onExitFromDashboard,
                        onIngredientClick = { navController.navigate(Screens.DetailsScreen.route + "/${it}") }
                    )
                }
            ) {
                DisplayError()
            }

        }

        composable(route = Screens.ProfileScreen.route) {
            NetworkStatus(
                onNetworkAvailable = {
                    ProfileScreen(
                        onLogoutClick = { navController.navigate(Screens.LoginScreen.route) },
                        onBackClick = { navController.popBackStack() },
                    )
                }
            ) {
                DisplayError()
            }
        }

        composable(route = Screens.RecipeScreen.route + "/{ingredients}" + "/{offset}") {
            NetworkStatus(
                onNetworkAvailable = {
                    val recipeIngredients = it.arguments?.getString("ingredients")
                    val recipeOffset = it.arguments?.getString("offset")?.toInt()
                    RecipeScreen(
                        offset = recipeOffset ?: 0,
                        ingredients = recipeIngredients ?: "",
                        onBackClick = { navController.popBackStack() },
                    )
                }
            ) {
                DisplayError()
            }

        }

        composable(route = Screens.FridgeScreen.route) {
            NetworkStatus(
                onNetworkAvailable = {
                    YourFridgeScreen(
                        onAddClick = { navController.navigate(Screens.AddIngredientScreen.route) },
                        onBackClick = { navController.popBackStack() },
                        onCardClick = { navController.navigate(Screens.DetailsScreen.route + "/${it}") },
                    )
                }) {
                DisplayError()
            }

        }

        composable(route = Screens.ScanScreen.route) {
            val parent = remember(it) { navController.getBackStackEntry(Screens.AddIngredientScreen.route) }
            val viewModel = hiltViewModel<ScanViewModel>(parent)

            NetworkStatus(
                onNetworkAvailable = {
                    Log.d("DEBUG", "Scan screen")
                    ScanScreen(
                        viewModel = viewModel,
                        onTextRecognised = {
                            navController.navigate(Screens.ScannedScreen.route + "/${it}")

                        },
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
            ) {
                DisplayError()
            }
        }

        composable(route = Screens.ScannedScreen.route + "/{text}") {
            val parent = remember(it) { navController.getBackStackEntry(Screens.AddIngredientScreen.route) }
            val viewModel = hiltViewModel<ScanViewModel>(parent)
            NetworkStatus(
                onNetworkAvailable = {
                    val text = it.arguments?.getString("text")
                    ScanResultScreen(
                        viewModel = viewModel,
                        text = text ?: "",
                        onBack = {
                            navController.popBackStack(
                                route = Screens.ScanScreen.route,
                                inclusive = false
                            )
                        },
                        onConfirmClick = {
                            navController.popBackStack(
                                route = Screens.AddIngredientScreen.route,
                                inclusive = false
                            )
                        }
                    )
                }
            ) {
                DisplayError()
            }
        }

        composable(route = Screens.AddIngredientScreen.route) {
            NetworkStatus(
                onNetworkAvailable = {
                    AddIngredientScreen(
                        onDone = { navController.popBackStack() },
                        onScanClick = {
                            navController.navigate(Screens.ScanScreen.route)
                        },
                    )
                }
            ) {
                DisplayError()
            }
        }

        composable(route = Screens.DetailsScreen.route + "/{id}") {
            NetworkStatus(
                onNetworkAvailable = {
                    val id = it.arguments?.getString("id")?.toInt() ?: 0
                    IngredientDetailsScreen(
                        ingredientId = id,
                        onBackButtonClick = {
                            navController.popBackStack()
                        },
                        onRecipeClick = { ing, offset -> navController.navigate(Screens.RecipeScreen.route + "/${ing}" + "/${offset}") }
                    )
                }
            ) {
                DisplayError()
            }
        }
    }
}