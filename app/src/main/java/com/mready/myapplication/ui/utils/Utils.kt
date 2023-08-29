package com.mready.myapplication.ui.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mready.myapplication.R
import com.mready.myapplication.models.Date
import com.mready.myapplication.models.Ingredient
import com.mready.myapplication.ui.theme.DarkAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar
import kotlin.math.abs
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

val ingredientToUrl = mapOf(
    "Onion" to "https://images.unsplash.com/photo-1618512496248-a07fe83aa8cb?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8b25pb258ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60",
    "Garlic" to "https://images.unsplash.com/photo-1625229466998-42ee9c597290?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8Z2FybGljfGVufDB8fDB8fHww&auto=format&fit=crop&w=500&q=60",
    "Olive Oil" to "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8b2xpdmUlMjBvaWx8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60",
    "Butter" to "https://images.unsplash.com/photo-1620567838034-f32ee85818aa?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8YnV0dGVyfGVufDB8fDB8fHww&auto=format&fit=crop&w=500&q=60",
    "Eggs" to "https://images.unsplash.com/photo-1491524062933-cb0289261700?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTJ8fGVnZ3N8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60",
    "Wheat Flour" to "https://images.unsplash.com/photo-1610725664285-7c57e6eeac3f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8ZmxvdXJ8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60",
    "Milk" to "https://plus.unsplash.com/premium_photo-1664647903517-70bb8213c743?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8bWlsa3xlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=500&q=60",
    "Lemon" to "https://images.unsplash.com/photo-1596181525841-8e8bae173eb0?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8bGVtb258ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60",
    "Rice" to "https://images.unsplash.com/photo-1610663711502-35f870cfaea2?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTB8fHJpY2V8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60",
    "Tomatoes" to "https://images.unsplash.com/photo-1558818498-28c1e002b655?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8dG9tYXRvZXN8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60",
    "Ginger" to "https://plus.unsplash.com/premium_photo-1675364893053-180a3c6e0119?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8Z2luZ2VyfGVufDB8fDB8fHww&auto=format&fit=crop&w=500&q=60",
    "Potatoes" to "https://plus.unsplash.com/premium_photo-1677528816982-673398569f03?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8N3x8cG90YXRvZXN8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60",
    "Carrots" to "https://images.unsplash.com/photo-1550411294-b3b1bd5fce1b?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8Y2Fycm90c3xlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=500&q=60",
    "Chicken" to "https://images.unsplash.com/photo-1587593810167-a84920ea0781?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8Y2hpY2tlbnxlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=500&q=60",
    "Beef" to "https://images.unsplash.com/photo-1603048297172-c92544798d5a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8YmVlZnxlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=500&q=60",
    "Pork" to "https://images.unsplash.com/photo-1623047437095-27418540c288?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8cG9ya3xlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=500&q=60",
    "Fish" to "https://images.unsplash.com/photo-1510130387422-82bed34b37e9?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTR8fGZpc2glMjAoZWRpYmxlKXxlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=500&q=60",
    "Cheese" to "https://images.unsplash.com/photo-1668104129962-66e931ec9a61?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MzN8fGNoZWVzZSUyMHNsaWNlfGVufDB8fDB8fHww&auto=format&fit=crop&w=500&q=60",
    "Bell Peppers" to "https://images.unsplash.com/photo-1592801062201-04fd6cf3d5ed?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NXx8YmVsbCUyMHBlcHBlcnN8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60",
    "Broccoli" to "https://images.unsplash.com/photo-1553175005-a1129d5c188c?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8YnJvY2NvbGl8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=500&q=60",
    "Corn" to "https://plus.unsplash.com/premium_photo-1667047165840-803e47970128?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8Y29ybnxlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=500&q=60",
    "Spinach" to "https://images.unsplash.com/photo-1578283326173-fbb0f83b59b2?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NXx8c3BpbmFjaHxlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=500&q=60",
    "Peas" to "https://images.unsplash.com/photo-1596564823703-d28706a620e8?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8cGVhc3xlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=500&q=60"
)

fun Date.toMillis(): Long {
    return (this.date + this.month * 30 + (this.year - 1970) * 365).toLong() * 24 * 60 * 60 * 1000
}

fun openYoutubeLink(context: Context, videoId: String) {
    val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$videoId"))
    try {
        context.startActivity(appIntent)
    } catch (ex: ActivityNotFoundException) {
        context.startActivity(webIntent)
    }
}

fun OpenYouTubeChannel(channelUrl: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(channelUrl)
    context.startActivity(intent)
}

@Composable
fun LoadingAnimation(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loading))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        restartOnPlay = true,
        iterations = Int.MAX_VALUE
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
        )
    }
}

@ExperimentalCoroutinesApi
fun createNetworkConnectivityService(context: Context): Flow<Boolean> = callbackFlow {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            trySend(true).isSuccess // Network is available
        }

        override fun onLost(network: Network) {
            trySend(false).isSuccess // Network is lost
        }
    }
    val networkRequest = NetworkRequest.Builder().build()

    connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

    awaitClose { connectivityManager.unregisterNetworkCallback(networkCallback) }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun NetworkStatus(
    onNetworkAvailable: @Composable () -> Unit,
    onNetworkUnavailable: @Composable () -> Unit
) {
    val context = LocalContext.current
    val networkConnectivityService = remember { createNetworkConnectivityService(context) }
    val isNetworkAvailable by networkConnectivityService.collectAsState(initial = true)

    if (isNetworkAvailable) {
        onNetworkAvailable()
    } else {
        onNetworkUnavailable()
    }
}

@Composable
fun DisplayError() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_wifi_off),
            contentDescription = null,
            tint = DarkAccent
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 88.dp),
            text = "No internet connection",
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = MainText,
        )

    }

}

fun Date.isExpired(): Boolean {
    val today = Calendar.getInstance()
    val expireDate = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }
    expireDate.set(this.year, this.month - 1, this.date)

    return today.after(expireDate)
}

fun Date.expiresRatherSoon(): Boolean {
    if (this.isExpired()) {
        return false
    }

    val today = Calendar.getInstance()
    val todayAsDate = Date(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH))

    val localDate1 = LocalDate.of(this.year, this.month, this.date)
    val localDate2 = LocalDate.of(todayAsDate.year, todayAsDate.month, todayAsDate.date)

    val daysApart = ChronoUnit.DAYS.between(localDate1, localDate2)

    return abs(daysApart) <= 2
}

fun List<Ingredient>.getFirstThreeDistinct(): List<Ingredient> {
    return this.filter { it.expireDate.isExpired() || it.expireDate.expiresRatherSoon() }.groupBy { it.expireDate }.values.take(3).flatten()
}