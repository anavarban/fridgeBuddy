package com.mready.myapplication.ui.fridge.addingredient

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
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