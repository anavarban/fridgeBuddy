package com.mready.myapplication

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mready.myapplication.navigation.Navigation
import com.mready.myapplication.notif.ResetNotificationWorker
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)

        val context = this.applicationContext

        val constraints = androidx.work.Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest =
            OneTimeWorkRequestBuilder<ResetNotificationWorker>().setConstraints(constraints).build()

        WorkManager.getInstance(context).enqueue(workRequest)
        Log.d("MainActivity", "WorkManager enqueued")

        setContent {
            MyApplicationTheme {
                WindowCompat.setDecorFitsSystemWindows(window, false)
                createAPISplashScreen()

                SplashScreen(postSplashScreenSystemBarsColor = Color.Transparent) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                    ) {
                        Scaffold(
                            containerColor = Background,
                        ) {
                            Navigation { this@MainActivity.finish() }
                        }
                    }
                }
            }
        }
        actionBar?.hide()
    }

    private fun createAPISplashScreen() {
        installSplashScreen()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashView ->
                ObjectAnimator.ofFloat(
                    splashView,
                    "alpha",
                    1f,
                    0f
                ).apply {
                    interpolator = AccelerateInterpolator()
                    duration = 500L
                    doOnEnd { splashView.remove() }
                    start()
                }
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun SplashScreen(
        postSplashScreenSystemBarsColor: Color,
        contentAfter: @Composable () -> Unit
    ) {
        var visible by remember { mutableStateOf(true) }
        val duration = remember { 500L }
        val systemUiController = rememberSystemUiController()

        LaunchedEffect(key1 = Unit) {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = true,
                isNavigationBarContrastEnforced = true
            )
        }

        LaunchedEffect(key1 = Unit, block = {
            launch {
                delay(duration)
                visible = false
            }
        })

        AnimatedContent(
            modifier = Modifier
                .fillMaxSize(),
            targetState = visible,
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = duration.toInt(),
                        easing = EaseIn
                    )
                ).togetherWith(
                    fadeOut(
                        animationSpec = tween(
                            durationMillis = duration.toInt(),
                            easing = EaseIn
                        )
                    )
                )
            },
            label = "splash"
        ) { state ->
            when (state) {
                true -> SplashScreenContent()
                false -> {
                    if (postSplashScreenSystemBarsColor != Color.Transparent) {
                        val color by transition.animateColor(
                            targetValueByState = {
                                when (it) {
                                    EnterExitState.PreEnter -> Color.Transparent
                                    EnterExitState.Visible -> postSplashScreenSystemBarsColor
                                    EnterExitState.PostExit -> postSplashScreenSystemBarsColor
                                }
                            },
                            label = "splash - status/nav bars color change"
                        )

                        SideEffect {
                            systemUiController.setSystemBarsColor(
                                color = color,
                                darkIcons = true,
                                isNavigationBarContrastEnforced = true
                            )
                        }
                    }

                    contentAfter()
                }
            }
        }
    }

    @Composable
    private fun SplashScreenContent() {
        Box(
            Modifier
                .fillMaxSize()
                .background(MainAccent)
        ) {
            Image(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(192.dp)
                    .align(Alignment.Center),
                painter = painterResource(id = R.drawable.ill_logo_splash_screen),
                contentDescription = null
            )
        }
    }
}

