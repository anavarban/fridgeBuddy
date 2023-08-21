package com.mready.myapplication.ui.dashboard

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.mready.myapplication.MainActivity
import com.mready.myapplication.R
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.SecondaryText
import com.mready.myapplication.ui.utils.BackPress
import kotlinx.coroutines.delay
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun DashboardScreen(
    notificationManager: NotificationManager,
    onSeeFridgeClick: () -> Unit,
    onRecipeClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onExit: () -> Unit,
    onIngredientClick: (Int) -> Unit
) {
    val dashboardViewModel: DashboardViewModel = hiltViewModel()
    val dashboardState = dashboardViewModel.dashboardFlow.collectAsState()

    var backPressState by remember { mutableStateOf<BackPress>(BackPress.Idle) }
    val context = LocalContext.current

    LaunchedEffect(key1 = backPressState) {
        if (backPressState == BackPress.InitialTouch) {
            delay(2000)
            backPressState = BackPress.Idle
        }
    }

    BackHandler {
        when (backPressState) {
            BackPress.Idle -> {
                backPressState = BackPress.InitialTouch
                Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
            }

            BackPress.InitialTouch -> {
                onExit()
            }
        }
    }

    var canLaunchNotifications by remember {
        mutableStateOf(false)
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        canLaunchNotifications = isGranted
    }

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        canLaunchNotifications = true
    } else {
        LaunchedEffect(key1 = null) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val intent = Intent(LocalContext.current, MainActivity::class.java)
    val contentIntent =
        PendingIntent.getActivity(LocalContext.current, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val notification = NotificationCompat.Builder(LocalContext.current, "fridge_channel")
        .setContentTitle("FridgeBuddy")
        .setContentText("Your ingredients will expire soon!")
        .setSmallIcon(R.drawable.ic_utensils)
        .setContentIntent(contentIntent)
        .setAutoCancel(true)
        .build()


//    LaunchedEffect(key1 = null) {
//        dashboardViewModel.loadDashboardWidgets()
//    }

    when (dashboardState.value) {
        DashboardState.Loading -> {
            Box (
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .align(Center),
                    color = Color.Red
                )
            }
        }

        is DashboardState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(top = 20.dp),
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 20.dp),
                    verticalAlignment = CenterVertically
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .border(1.dp, MainAccent, CircleShape)
                            .clip(CircleShape)
                            .size(56.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null,
                                onClick = onProfileClick
                            ),
                        model = dashboardViewModel.currentUser.value?.photoUrl ?: "",
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        modifier = Modifier.padding(start = 12.dp),
                        text = stringResource(
                            id = R.string.dashboard_hello_msg,
                            dashboardViewModel.currentUser.value?.displayName ?: ""
                        ),
                        fontSize = 16.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = SecondaryText
                    )
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, start = 20.dp),
                    text = stringResource(id = R.string.dashboard_title_prompt),
                    textAlign = TextAlign.Left,
                    fontSize = 24.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = MainText
                )

                val items = (dashboardState.value as DashboardState.Success).widgets.sortedBy {
                    when (it) {
                        is RecommendedWidgetItemViewModel -> 0
                        is FridgeWidgetItemViewModel -> 1
                        else -> 2
                    }
                }
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(items) {
                        when (it) {
                            is RecommendedWidgetItemViewModel -> {
                                RecommendedRecipes(
                                    recipes = it.displayRecipes,
                                    ingredientsToExpire = it.soonToExpireIngredients,
                                    onRecipeClick = onRecipeClick
                                )
                            }

                            is PopularWidgetItemViewModel -> {
                                PopularRecipes(recipeUrls = it.displayRecipes)
                            }

                            is FridgeWidgetItemViewModel -> {
                                FridgeIngredients(
                                    ingredients = it.displayIngredients,
                                    onSeeFridgeClick = onSeeFridgeClick,
                                    onIngredientClick = onIngredientClick
                                )

                                val date = Calendar.getInstance()
                                val formattedDate = com.mready.myapplication.models.Date(
                                    year = date.get(Calendar.YEAR),
                                    month = date.get(Calendar.MONTH) + 1,
                                    date = date.get(Calendar.DAY_OF_MONTH)
                                )

                                if (it.displayIngredients.isNotEmpty()) {

                                    val first = it.displayIngredients.first()
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
                                    // TODO: make it so it only notifies once
                                    if ( (compareDates <= 0) && canLaunchNotifications) {
                                        notificationManager.notify(
                                            it.displayIngredients.first().id,
                                            notification
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        DashboardState.Error -> {
            Text(
                modifier = Modifier.padding(vertical = 48.dp, horizontal = 16.dp),
                text = stringResource(id = R.string.generic_error),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = MainText
            )
        }
    }


}


