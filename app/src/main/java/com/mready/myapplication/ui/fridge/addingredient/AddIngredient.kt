package com.mready.myapplication.ui.fridge.addingredient

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.mready.myapplication.R
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.LightAccent
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddIngredientScreen(
    onDone: () -> Unit
) {
    val addIngredientViewModel: AddIngredientViewModel = hiltViewModel()

    val addState = addIngredientViewModel.addIngredientUiState.collectAsState()

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        3
    }
    val coroutineScope = rememberCoroutineScope()

    var userScrollEnabled by remember {
        mutableStateOf(false)
    }

    var progress by remember {
        mutableStateOf(0f)
    }

    var showPopUp by remember {
        mutableStateOf(false)
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .safeDrawingPadding()
    ) {
        LaunchedEffect(key1 = pagerState.currentPage) {
            when (pagerState.currentPage) {
                0 -> progress = 0f
                1 -> progress = 0.33f
                2 -> progress = 0.66f
            }
        }

        LinearProgressIndicator(
            modifier = Modifier
                .padding(top = 48.dp, bottom = 32.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            progress = progress,
            color = MainAccent,
            trackColor = LightAccent,
        )


        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp),
            state = pagerState,
            pageSpacing = 0.dp,
            userScrollEnabled = userScrollEnabled,
            reverseLayout = false,
            beyondBoundsPageCount = 0,
            key = null
            ){
                when (it) {
                    0 -> {
                        AddType(
                            startingType = addIngredientViewModel.type,
                            user = addIngredientViewModel.currentUser?.email ?: "",
                            onNextClick = { _, type ->
                                addIngredientViewModel.type = type
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(1)
                                }
                                userScrollEnabled = true
                            }
                        )
                    }

                    1 -> {
                        AddAmount(
                            user = addIngredientViewModel.currentUser?.email ?: "",
                            ingredientName = addIngredientViewModel.type
                        ) { _, _, unit, amount ->
                            addIngredientViewModel.unit = unit
                            addIngredientViewModel.amount = amount
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(2)
                            }
                        }
                    }

                    2 -> {
                        AddExpireDate(
                            user = addIngredientViewModel.currentUser?.email ?: "",
                            ingredientName = addIngredientViewModel.type,
                            unit = addIngredientViewModel.unit,
                            amount = addIngredientViewModel.amount,
                            onDateSelected = { date ->
                                addIngredientViewModel.date = date
                            },
                            onDoneClick = {
                                addIngredientViewModel.addIngredient()
                                onDone()
                                progress = 1f
                            }
                        )
                    }
                }
            }


        IconButton(
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.TopEnd),
            onClick = { showPopUp = true }
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null,
                tint = MainAccent
            )
        }

    }

    if (showPopUp) {
        AlertDialog(
            onDismissRequest = { showPopUp = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = Background)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier
                        .size(72.dp)
                        .border(4.dp, MainAccent, CircleShape),
                    imageVector = Icons.Outlined.Close,
                    contentDescription = null,
                    tint = MainAccent
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.cancel_adding_ingredient),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontFamily = Poppins,
                    color = MainText
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier,
                    onClick = {
                        showPopUp = false
                        onDone()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MainAccent
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.generic_exit),
                        fontSize = 20.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}