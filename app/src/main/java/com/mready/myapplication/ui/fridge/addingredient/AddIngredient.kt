package com.mready.myapplication.ui.fridge.addingredient

import android.widget.ProgressBar
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mready.myapplication.ui.theme.LightAccent
import com.mready.myapplication.ui.theme.MainAccent
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddIngredientScreen(
    onDone: () -> Unit
) {
    val addIngredientViewModel: AddIngredientViewModel = hiltViewModel()

    val addState = addIngredientViewModel.addIngredientUiState.collectAsState()

    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()

    var userScrollEnabled by remember {
        mutableStateOf(false)
    }

    var progress by remember {
        mutableStateOf(0f)
    }

    Box {
        HorizontalPager(
            pageCount = 3,
            state = pagerState,
            userScrollEnabled = userScrollEnabled
        ) {
            when (it) {
                0 -> AddType(
                    user = addIngredientViewModel.currentUser?.email ?: "",
                    onNextClick = { _, type ->
                        addIngredientViewModel.type = type
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                        userScrollEnabled = true
                        progress = 0.33f
                    }
                )

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
                        progress = 0.66f
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

        LinearProgressIndicator(
            modifier = Modifier
                .padding(bottom = 80.dp)
                .fillMaxWidth(.8f)
                .align(Alignment.BottomCenter),
            progress = progress,
            color = MainAccent,
            trackColor = LightAccent,

        )
    }

}