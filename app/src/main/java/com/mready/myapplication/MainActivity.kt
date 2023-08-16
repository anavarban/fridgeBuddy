package com.mready.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.FirebaseApp
import com.mready.myapplication.navigation.Navigation
import com.mready.myapplication.ui.fridge.FridgeViewModel
import com.mready.myapplication.ui.fridge.YourFridgeScreen
import com.mready.myapplication.ui.onboarding.OnboardingViewModel
import com.mready.myapplication.ui.profile.ProfileScreen
import com.mready.myapplication.ui.recipes.RecipeScreen
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val onboardingViewModel by viewModels<OnboardingViewModel>()


    private val fridgeViewModel by viewModels<FridgeViewModel>()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            MyApplicationTheme {

                WindowCompat.setDecorFitsSystemWindows(window, false)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Scaffold(
                        containerColor = Background,
//                       bottomBar = { BottomNavBar() }
                    ) {
//                        RecipeScreen("Pasta")
//                        DashboardScreen()
                        //LoginScreen()
//                        SignUpScreen()
//                        StartScreen()
//                        LoginScreen()
//                        AddType()
//                        AddAmount(ingredientName = "Cheese")
//                    AddExpireDate(ingredientName = "Cheese")
//                        YourFridgeScreen()
                        Navigation(
                            onboardingViewModel = onboardingViewModel,
                            fridgeViewModel = fridgeViewModel,
                            onExitFromDashboard = { this@MainActivity.finish() }
                        )
//                        ProfileScreen(onLogoutClick = { /*TODO*/ }, onboardingViewModel = onboardingViewModel)
                    }
                }
            }
        }
    }
}
