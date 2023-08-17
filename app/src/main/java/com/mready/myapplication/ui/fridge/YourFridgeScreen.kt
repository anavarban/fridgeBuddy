package com.mready.myapplication.ui.fridge

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mready.myapplication.R
import com.mready.myapplication.ui.dashboard.IngredientItem
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.SecondaryText

@Composable
fun YourFridgeScreen(
    onAddClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    BackHandler {
        onBackClick()
    }

    val fridgeViewModel: FridgeViewModel = hiltViewModel()


    val fridgeState = fridgeViewModel.fridgeFlow.collectAsState()

    LaunchedEffect(key1 = null) {
        fridgeViewModel.loadIngredients()
    }

    when (fridgeState.value) {
        FridgeIngredientsUiState.Error -> {

        }

        FridgeIngredientsUiState.Loading -> {

        }

        is FridgeIngredientsUiState.Success -> {

            val ingredients = (fridgeState.value as FridgeIngredientsUiState.Success).ingredients

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp, start = 20.dp, end = 20.dp),
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(top = 2.dp, end = 8.dp)
                                .size(40.dp)
                                .clickable (
                                    interactionSource = MutableInteractionSource(),
                                    indication = null,
                                    onClick = {
                                        onBackClick()
                                    }
                                ),
                            imageVector = Icons . Outlined . KeyboardArrowLeft,
                            contentDescription = null,
                            tint = MainText
                        )

                        Text(
                            modifier = Modifier,
                            text = stringResource(id = R.string.fridge_title),
                            fontSize = 28.sp,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.SemiBold,
                            color = MainText
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp, start = 20.dp, end = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.fridge_soon_to_expire),
                            textAlign = TextAlign.Left,
                            fontSize = 24.sp,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.SemiBold,
                            color = SecondaryText
                        )
                    }

//            FridgeSoonToExpire()

                    LazyRow(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .align(Alignment.Start),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
                    ) {
                        items(ingredients?.sortedWith { o1, o2 ->
                            if (o1.expireDate.year == o2.expireDate.year) {
                                if (o1.expireDate.month == o2.expireDate.month) {
                                    o1.expireDate.date - o2.expireDate.date
                                } else {
                                    o1.expireDate.month - o2.expireDate.month
                                }
                            } else {
                                o1.expireDate.year - o2.expireDate.year
                            }
                        }?.take(3) ?: emptyList()) {
                            IngredientItem(
                                modifier = Modifier.width(120.dp),
                                ingredient = it
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp, start = 20.dp, end = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.fridge_all_ingredients),
                            textAlign = TextAlign.Left,
                            fontSize = 24.sp,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.SemiBold,
                            color = SecondaryText
                        )

                        //todo edit here???

                    }

                    LazyVerticalGrid(
                        modifier = Modifier.padding(top = 20.dp),
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(
                            top = 1.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 40.dp
                        )
                    ) {
                        items(ingredients ?: emptyList()) {
                            IngredientItem(
                                modifier = Modifier.width(120.dp),
                                ingredient = it,
                                showEditButton = true,
                                onEditClick = {
                                    fridgeViewModel.deleteIngredient(it)
                                }
                            )
                        }
                    }

                }

                FloatingActionButton(
                    modifier = Modifier
                        .fillMaxWidth(.7f)
                        .align(Alignment.BottomCenter),
                    onClick = { onAddClick(fridgeViewModel.currentUser?.email ?: "") },
                    containerColor = MainAccent,
                    contentColor = Background,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.fridge_add),
                        fontSize = 18.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

        }
    }


}