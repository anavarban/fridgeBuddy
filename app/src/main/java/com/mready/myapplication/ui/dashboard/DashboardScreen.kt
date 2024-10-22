package com.mready.myapplication.ui.dashboard

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mready.myapplication.R
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.SecondaryText
import com.mready.myapplication.ui.utils.BackPress
import kotlinx.coroutines.delay

@Composable
fun DashboardScreen(
    onSeeFridgeClick: () -> Unit,
    onIngredientEditClick: (Int) -> Unit,
    onRecipeClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onExit: () -> Unit
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


    LaunchedEffect(key1 = null) {
        dashboardViewModel.loadDashboardWidgets()
    }

    when (dashboardState.value) {
        DashboardState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 32.dp),
                color = MainAccent
            )
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
                        model = dashboardViewModel.currentUser?.photoUrl ?: "",
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        modifier = Modifier.padding(start = 12.dp),
                        text = stringResource(
                            id = R.string.dashboard_hello_msg,
                            dashboardViewModel.currentUser?.displayName ?: ""
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
                                    onSeeFridgeClick = onSeeFridgeClick
                                )
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


