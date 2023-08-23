package com.mready.myapplication.notif

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ResetNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val sharedPreferences = context.getSharedPreferences("NotificationPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("hasNotified", false)
        editor.apply()
    }
}