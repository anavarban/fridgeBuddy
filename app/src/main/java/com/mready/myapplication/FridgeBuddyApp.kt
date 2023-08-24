package com.mready.myapplication

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FridgeBuddyApp: Application() {

        override fun onCreate() {
            super.onCreate()
            FirebaseApp.initializeApp(this)
//            WorkManager.initialize(this, Configuration.Builder().build())
        }
}