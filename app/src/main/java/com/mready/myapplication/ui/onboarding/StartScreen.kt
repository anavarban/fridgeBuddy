package com.mready.myapplication.ui.onboarding

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mready.myapplication.R
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.SecondaryText
import com.mready.myapplication.ui.utils.BackPress
import com.mready.myapplication.ui.utils.DotsIndicator
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StartScreen(
    onClick: () -> Unit,
    onExit: () -> Unit
) {
    var backPressState by remember { mutableStateOf<BackPress>(BackPress.Idle) }
    val context = LocalContext.current

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

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        3
    }

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth(),
            state = pagerState,
            userScrollEnabled = true
        ) {
            when (it) {
                0 -> {
                    StartScreenPageOne(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        }
                    )
                }

                1 -> {
                    StartScreenPageTwo(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(2)
                            }
                        }
                    )
                }

                2 -> {
                    StartScreenPageThree(
                        onClick = onClick
                    )
                }
            }
        }

        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 32.dp, end = 20.dp)
                .shadow(3.dp, CircleShape),
            onClick = onClick,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = Background,
                contentColor = MainAccent
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null
            )
        }
    }
}

@Composable
fun StartScreenPageOne(onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {


        Box (
            modifier = Modifier.fillMaxHeight(.7f)
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = "https://images.unsplash.com/photo-1493770348161-369560ae357d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80",
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 240.dp)
                    .background(
                        Brush.verticalGradient(
                            0.0f to Color.Transparent,
                            1.0f to Background
                        )
                    )
                    .align(Alignment.BottomCenter)
            ) {}

        }


        Surface(
            modifier = Modifier
                .padding(bottom = 8.dp),
            shape = CircleShape,
            color = MainAccent.copy(alpha = 0.5f)
        ) {
            DotsIndicator(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                totalDots = 3,
                selectedIndex = 0,
                dotSize = 8.dp,
                selectedColor = MainAccent,
                unSelectedColor = Color.White
            )
        }

        Text(
            text = stringResource(id = R.string.onboarding_start_title_message),
            fontSize = 24.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = MainText
        )

        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = stringResource(id = R.string.onboarding_start_intro_message),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontFamily = Poppins,
            color = SecondaryText
        )

        Button(
            modifier = Modifier.fillMaxWidth(.7f),
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MainAccent
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.generic_next),
                fontSize = 18.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun StartScreenPageTwo(onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box (
            modifier = Modifier.fillMaxHeight(.7f)
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = "https://i.pinimg.com/564x/88/fa/4a/88fa4a03f3f448feef5f450573ef999c.jpg",
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 240.dp)
                    .background(
                        Brush.verticalGradient(
                            0.0f to Color.Transparent,
                            1.0f to Background
                        )
                    )
                    .align(Alignment.BottomCenter)
            ) {}

        }


        Surface(
            modifier = Modifier
                .padding(bottom = 8.dp),
            shape = CircleShape,
            color = MainAccent.copy(alpha = 0.5f)
        ) {
            DotsIndicator(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                totalDots = 3,
                selectedIndex = 1,
                dotSize = 8.dp,
                selectedColor = MainAccent,
                unSelectedColor = Color.White
            )
        }

        Text(
            text = stringResource(id = R.string.onboarding_start_title_message_2),
            fontSize = 24.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = MainText
        )

        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = stringResource(id = R.string.onboarding_start_intro_message_2),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontFamily = Poppins,
            color = SecondaryText
        )

        Button(
            modifier = Modifier.fillMaxWidth(.7f),
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MainAccent
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.generic_next),
                fontSize = 18.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun StartScreenPageThree(onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box (
            modifier = Modifier.fillMaxHeight(.7f)
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = "https://images.unsplash.com/photo-1505576399279-565b52d4ac71?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NDV8fGZvb2QlMjBpbmdyZWRpZW50c3xlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=500&q=60",
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 240.dp)
                    .background(
                        Brush.verticalGradient(
                            0.0f to Color.Transparent,
                            1.0f to Background
                        )
                    )
                    .align(Alignment.BottomCenter)
            ) {}

        }



        Surface(
            modifier = Modifier
                .padding(bottom = 8.dp),
            shape = CircleShape,
            color = MainAccent.copy(alpha = 0.5f)
        ) {
            DotsIndicator(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                totalDots = 3,
                selectedIndex = 2,
                dotSize = 8.dp,
                selectedColor = MainAccent,
                unSelectedColor = Color.White
            )
        }

        Text(
            text = stringResource(id = R.string.onboarding_start_title_message_3),
            fontSize = 24.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = MainText
        )

        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = stringResource(id = R.string.onboarding_start_intro_message_3),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontFamily = Poppins,
            color = SecondaryText
        )

        Button(
            modifier = Modifier.fillMaxWidth(.7f),
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MainAccent
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.onboarding_button_let_s_go),
                fontSize = 18.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
