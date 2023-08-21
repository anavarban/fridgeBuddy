package com.mready.myapplication

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.mready.myapplication.navigation.Navigation
import com.mready.myapplication.navigation.Screens
import com.mready.myapplication.ui.fridge.FridgeViewModel
import com.mready.myapplication.ui.onboarding.LoginViewModel
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val channelId = "fridge_channel"
        val channelName = "FridgeBuddy"
        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        setContent {
            MyApplicationTheme {
                WindowCompat.setDecorFitsSystemWindows(window, false)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Scaffold(
                        containerColor = Background,
                    ) {
                        Navigation(
                            notificationManager = notificationManager,
                            onExitFromDashboard = { this@MainActivity.finish() }
                        )
                    }
                }
            }
        }
    }
}
