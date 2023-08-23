package com.mready.myapplication.ui.fridge

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.mready.myapplication.R
import com.mready.myapplication.models.Ingredient
import com.mready.myapplication.ui.dashboard.IngredientItem
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.SecondaryText
import com.mready.myapplication.ui.utils.LoadingAnimation

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun YourFridgeScreen(
    onAddClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onCardClick: (Int) -> Unit
) {
    BackHandler {
        onBackClick()
    }

    val fridgeViewModel: FridgeViewModel = hiltViewModel()


    val fridgeState = fridgeViewModel.fridgeFlow.collectAsState()

    LaunchedEffect(key1 = null) {
        fridgeViewModel.loadIngredients()
    }

    var showPopUp by remember {
        mutableStateOf(false)
    }

    var toDelete by remember {
        mutableStateOf(0)
    }

    when (fridgeState.value) {
        FridgeIngredientsUiState.Error -> {
            //todo
        }

        FridgeIngredientsUiState.Loading -> {
            LoadingAnimation()
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp, start = 20.dp, end = 20.dp),
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(40.dp)
                                .clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null,
                                    onClick = {
                                        onBackClick()
                                    }
                                ),
                            imageVector = Icons.Outlined.KeyboardArrowLeft,
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

                    if (ingredients.isNullOrEmpty()) {
                        Text(
                            modifier = Modifier
                                .padding(top = 80.dp, start = 20.dp, end = 20.dp)
                                .align(Alignment.CenterHorizontally),
                            text = stringResource(id = R.string.fridge_empty),
                            textAlign = TextAlign.Left,
                            fontSize = 20.sp,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.SemiBold,
                            color = SecondaryText,
                        )
                    } else {
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

                        LazyRow(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .align(Alignment.Start),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
                        ) {
                            items(ingredients.sortedWith { o1, o2 ->
                                if (o1.expireDate.year == o2.expireDate.year) {
                                    if (o1.expireDate.month == o2.expireDate.month) {
                                        o1.expireDate.date - o2.expireDate.date
                                    } else {
                                        o1.expireDate.month - o2.expireDate.month
                                    }
                                } else {
                                    o1.expireDate.year - o2.expireDate.year
                                }
                            }.take(3)) {
                                IngredientItem(
                                    modifier = Modifier
                                        .width(120.dp)
                                        .clickable { onCardClick(it.id) },
                                    showDeleteButton = false,
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
                        }


                        LazyVerticalGrid(
                            modifier = Modifier
                                .padding(top = 20.dp),
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
                            items(ingredients, key = { it.id }) {
                                IngredientItem(
                                    modifier = Modifier
                                        .width(120.dp)
                                        .animateItemPlacement()
                                        .clickable { onCardClick(it.id) },
                                    ingredient = it,
                                    showDeleteButton = true,
                                    onDeleteClick = {
                                        showPopUp = true
                                        toDelete = ingredients.indexOf(it)
                                    }
                                )
                            }
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

                if (showPopUp && ingredients != null) {
                    FridgeDeletePopUp(
                        ingredient = ingredients[toDelete],
                        onDismissRequest = { showPopUp = false },
                        onDeleteClick = {
                            fridgeViewModel.deleteIngredient(it)
                            showPopUp = false
                        }
                    )
                }

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FridgeDeletePopUp(
    ingredient: Ingredient,
    onDismissRequest: () -> Unit,
    onDeleteClick: (Ingredient) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
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
                text = stringResource(id = R.string.delete_ingredient),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontFamily = Poppins,
                color = MainText
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier,
                onClick = {
                    onDismissRequest()
                    onDeleteClick(ingredient)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MainAccent
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.generic_delete),
                    fontSize = 20.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}