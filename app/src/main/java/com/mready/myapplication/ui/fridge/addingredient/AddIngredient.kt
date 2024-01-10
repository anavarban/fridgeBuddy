package com.mready.myapplication.ui.fridge.addingredient

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.mready.myapplication.R
import com.mready.myapplication.ui.fridge.scan.ScanViewModel
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.Error
import com.mready.myapplication.ui.theme.LightAccent
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.utils.FridgeBuddyButton

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class
)
@Composable
fun AddIngredientScreen(
    scanViewModel: ScanViewModel = hiltViewModel(),
    onScanClick: () -> Unit,
    onDone: () -> Unit
) {
    val addIngredientViewModel: AddIngredientViewModel = hiltViewModel()

    val scannedImageTitle by scanViewModel.titleFlow.collectAsState()
    val scannedImagePath by scanViewModel.capturedImagePathFlow.collectAsState()

    if (scannedImageTitle != "") {
        addIngredientViewModel.type = scannedImageTitle
        scanViewModel.updateTitle("")
    }

    if (scannedImagePath != "") {
        addIngredientViewModel.picturePath = scannedImagePath
        scanViewModel.updateCapturedImage("")
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    var page by remember {
        mutableStateOf(0)
    }

    val progress by animateFloatAsState(
        targetValue = when (page) {
            0 -> 0f
            1 -> 0.33f
            2 -> 0.66f
            else -> 1f
        },
        animationSpec = tween(500),
        label = "animate*AsState"
    )

    var showPopUp by remember {
        mutableStateOf(false)
    }

    fun handleBack() {
        if (page == 0) {
            showPopUp = true
        } else {
            page -= 1
        }
    }

    BackHandler {
        handleBack()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 52.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .padding(start = 12.dp)
                .size(32.dp)
                .align(Alignment.Start)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = {
                        handleBack()
                    }
                ),
            imageVector = Icons.Outlined.KeyboardArrowLeft,
            contentDescription = null,
            tint = MainAccent
        )


        Column(
            modifier = Modifier
                .padding(top = 16.dp)
                .height(4.dp)
                .fillMaxWidth(),
        ) {
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth(),
                visible = page != 0,
                enter = fadeIn(
                    tween(500)
                ),
                exit = fadeOut(
                    tween(500)
                )
            ) {
                LinearProgressIndicator(
                    progress = progress,
                    color = MainAccent,
                    trackColor = LightAccent,
                )
            }
        }

        AnimatedContent(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            targetState = page,
            label = "",
            transitionSpec = {
                if (targetState > 0) {
                    slideInHorizontally { it }.togetherWith(slideOutHorizontally { -it })
                } else {
                    slideInHorizontally { -it }.togetherWith(
                        slideOutHorizontally { it })
                }
            }
        ) {
            when (it) {
                0 -> {
                    AddType(
                        startingType = addIngredientViewModel.type,
                        user = addIngredientViewModel.currentUser?.email ?: "",
                        onNextClick = { _, type ->
                            addIngredientViewModel.type = type
                            page = 1
                        },
                        onScanClick = {
                            onScanClick()
                        }
                    )
                }

                1 -> {
                    AddAmount(
                        user = addIngredientViewModel.currentUser?.email ?: "",
                        ingredientName = addIngredientViewModel.type
                    ) { _, _, unit, amount ->
                        keyboardController?.hide()
                        addIngredientViewModel.unit = unit
                        addIngredientViewModel.amount = amount
                        page = 2
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
                        }
                    )
                }
            }
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
                        .border(4.dp, Error, CircleShape),
                    imageVector = Icons.Outlined.Close,
                    contentDescription = null,
                    tint = Error
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

                FridgeBuddyButton(
                    text = stringResource(id = R.string.generic_exit),
                    containerColor = Error,
                    onClick = {
                        showPopUp = false
                        onDone()
                    }
                )
            }
        }
    }
}