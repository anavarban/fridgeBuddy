package com.mready.myapplication.ui.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.system.exitProcess

const val clientId = "835229504494-228oa534qthjun48rr48obm7ns2nitam.apps.googleusercontent.com"

internal enum class signUpFields {
    NAME, EMAIL, PASSWORD
}

internal enum class LoginFields {
    EMAIL, PASSWORD
}
sealed class BackPress {
    object Idle : BackPress()
    object InitialTouch : BackPress()
}

@Composable

 fun DoubleBackPressToExit() {
    var backPressState by remember { mutableStateOf<BackPress>(BackPress.Idle) }
    val context = LocalContext.current

    LaunchedEffect(key1 = backPressState) {
        if (backPressState == BackPress.InitialTouch) {
            delay(2000)
            backPressState = BackPress.Idle
        }
    }

    BackHandler(backPressState == BackPress.Idle) {
        when (backPressState) {
            BackPress.Idle -> {
                backPressState = BackPress.InitialTouch
                Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
            }
            BackPress.InitialTouch -> {
                exitProcess(0)
            }
        }
    }
}

