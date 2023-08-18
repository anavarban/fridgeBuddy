package com.mready.myapplication

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationCompat
import androidx.core.view.WindowCompat
import com.mready.myapplication.navigation.Navigation
import com.mready.myapplication.ui.fridge.FridgeViewModel
import com.mready.myapplication.ui.onboarding.OnboardingViewModel
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val onboardingViewModel by viewModels<OnboardingViewModel>()


    private val fridgeViewModel by viewModels<FridgeViewModel>()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val channelId = "fridge_channel"
        val channelName = "FridgeBuddy"
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("My Notification")
            .setContentText("This is a notification")
            .setSmallIcon(R.drawable.ic_utensils)
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .build()

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
                            onboardingViewModel = onboardingViewModel,
                            onExitFromDashboard = { this@MainActivity.finish() }
                        )
                    }
                }
            }
        }
    }
}
