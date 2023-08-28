package com.mready.myapplication.notif

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.mready.myapplication.FridgeBuddyApp
import com.mready.myapplication.MainActivity
import com.mready.myapplication.R
import com.mready.myapplication.auth.AuthRepository
import com.mready.myapplication.data.FridgeDatabase
import com.mready.myapplication.data.FridgeIngredientsRepo
import com.mready.myapplication.data.FridgeIngredientsRepoImpl
import com.mready.myapplication.models.toIngredient
import com.mready.myapplication.ui.utils.getFirstThreeDistinct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.util.Calendar
import kotlin.random.Random

class ResetNotificationWorker(
    val context: Context,
    params: WorkerParameters,
) : Worker(context, params) {
    override fun doWork(): Result {
        Log.d("ResetNotificationWorker", "Resetting notifications")

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val user = FirebaseAuth.getInstance().currentUser ?: return Result.success()

        val fridgeDao = FridgeDatabase.getDatabase(context).fridgeDao()

        val fridgeIngredientsRepo = FridgeIngredientsRepoImpl(fridgeDao)

        val userEmail = user.email ?: return Result.success()

        val coroutineScope = CoroutineScope(Dispatchers.IO)

        coroutineScope.launch {
            val list = fridgeIngredientsRepo.getUserIngredients(userEmail).stateIn(coroutineScope).value

            val intent = Intent(context, MainActivity::class.java)
            val contentIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            val notification = NotificationCompat.Builder(context, "fridge_channel")
                .setContentTitle("FridgeBuddy")
                .setContentText("Your ingredients will expire soon!")
                .setSmallIcon(R.drawable.ic_utensils)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            val ingredients = list.map { elem -> elem.toIngredient() }

            val date = Calendar.getInstance()
            val formattedDate = com.mready.myapplication.models.Date(
                year = date.get(Calendar.YEAR),
                month = date.get(Calendar.MONTH) + 1,
                date = date.get(Calendar.DAY_OF_MONTH)
            )

            val displayIngredients = ingredients
//                .filter {
//                    it.expireDate.year == formattedDate.year &&
//                            (it.expireDate.month == formattedDate.month
//                                    || (it.expireDate.month == formattedDate.month + 1 && it.expireDate.date < formattedDate.date))
//                }
                .sortedWith { o1, o2 ->
                    if (o1.expireDate.year == o2.expireDate.year) {
                        if (o1.expireDate.month == o2.expireDate.month) {
                            o1.expireDate.date - o2.expireDate.date
                        } else {
                            o1.expireDate.month - o2.expireDate.month
                        }
                    } else {
                        o1.expireDate.year - o2.expireDate.year
                    }
                }.getFirstThreeDistinct()
//                .take(3)
//            }.takeWhile { it.expireDate.year == formattedDate.year &&
//                    (it.expireDate.month == formattedDate.month
//                            || (it.expireDate.month == formattedDate.month + 1 && it.expireDate.date < formattedDate.date))
//            }

            if (displayIngredients.isNotEmpty()) {

                val first = displayIngredients.first()
                val compareDates =
                    if (first.expireDate.year == formattedDate.year) {
                        if (first.expireDate.month == formattedDate.month) {
                            first.expireDate.date - formattedDate.date - 2
                        } else {
                            first.expireDate.month - formattedDate.month
                        }
                    } else {
                        first.expireDate.year - formattedDate.year
                    }

                if (compareDates <= 0) {
                    notificationManager.notify(
                        Random.nextInt(),
                        notification
                    )
                }
            }
        }
        return Result.success()
    }
}