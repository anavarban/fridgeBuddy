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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.mready.myapplication.R
import com.mready.myapplication.models.mockedIngredients
import com.mready.myapplication.ui.onboarding.OnboardingViewModel
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.SecondaryText
import com.mready.myapplication.ui.utils.BackPress
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.delay

@Composable
fun DashboardScreen(
    onSeeRecipesClick: () -> Unit,
    onSeeFridgeClick: () -> Unit,
    onIngredientEditClick: (Int) -> Unit,
    onRecipeClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onboardingViewModel: OnboardingViewModel,
    onExit: () -> Unit
) {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(top = 20.dp)
            .verticalScroll(rememberScrollState())
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
                model = onboardingViewModel.currentUser?.photoUrl ?: "",
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = stringResource(
                    id = R.string.dashboard_hello_msg,
                    onboardingViewModel.currentUser?.displayName ?: ""
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

        DashboardPrevRecipes(onSeeRecipesClick)

        Spacer(modifier = Modifier.height(32.dp))

        DashboardYourFridge(onSeeFridgeClick)


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.dashboard_popular_recipes),
                textAlign = TextAlign.Left,
                fontSize = 20.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = MainText
            )

            Row(
                modifier = Modifier
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = { /*TODO*/ },
                    ),
                verticalAlignment = CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = stringResource(id = R.string.generic_see_more),
                    textAlign = TextAlign.Left,
                    fontSize = 20.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = MainAccent
                )

                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MainAccent
                )
            }
        }

        LazyRow(
            modifier = Modifier.padding(top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 32.dp)
        ) {
            items(
                listOf(
                    "https://www.youtube.com/watch?v=brqY65Hp15M&pp=ygUGamFtaWxh",
                    "https://www.youtube.com/watch?v=df1QU5kQMyg&pp=ygUGamFtaWxh"
                )
            ) {
                YoutubeScreen(
                    modifier = Modifier
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .size(width = 320.dp, height = 180.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    videoId = it
                )
            }
        }


    }
}

@Composable
fun YoutubeScreen(
    videoId: String,
    modifier: Modifier
) {
    val context = LocalContext.current
    AndroidView(
        modifier = modifier,
        factory = {
            val view = YouTubePlayerView(it)
            view.addYouTubePlayerListener(
                object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        super.onReady(youTubePlayer)
                        youTubePlayer.loadVideo(videoId, 0f)
                    }
                }
            )
            view
        }
    )
}


@Composable
fun DashboardPrevRecipes(
    onSeeRecipesClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.dashboard_previous_recipes),
            textAlign = TextAlign.Left,
            fontSize = 20.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = MainText
        )

        Row(
            modifier = Modifier
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = onSeeRecipesClick
                ),
            verticalAlignment = CenterVertically

        ) {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(id = R.string.generic_see_all),
                textAlign = TextAlign.Left,
                fontSize = 20.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = MainAccent
            )

            Icon(
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = MainAccent
            )
        }
    }

    LazyRow(
        modifier = Modifier.padding(top = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
    ) {
//        items(mockRecipes) {
//            RecipeItem(recipe = it)
//        }
    }
}

@Composable
fun DashboardYourFridge(
    onSeeFridgeClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.dashboard_your_fridge),
            textAlign = TextAlign.Left,
            fontSize = 20.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = MainText
        )

        Row(
            modifier = Modifier
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = onSeeFridgeClick
                ),
            verticalAlignment = CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(id = R.string.generic_see_all),
                textAlign = TextAlign.Left,
                fontSize = 20.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = MainAccent
            )

            Icon(
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = MainAccent
            )
        }
    }

    LazyRow(
        modifier = Modifier.padding(top = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
    ) {
        items(mockedIngredients) {
            IngredientItem(ingredient = it)
        }
    }
}

