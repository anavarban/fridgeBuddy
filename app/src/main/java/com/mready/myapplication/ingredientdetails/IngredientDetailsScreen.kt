package com.mready.myapplication.ingredientdetails

import android.content.Context
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.allViews
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mready.myapplication.R
import com.mready.myapplication.models.Ingredient
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.Error
import com.mready.myapplication.ui.theme.LightAccent
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.SecondaryText

@Composable
fun IngredientDetailsScreen(
    ingredientId: Int,
    onBackButtonClick: () -> Unit
) {

    val ingredientDetailsViewModel: IngredientDetailsViewModel = hiltViewModel()

    val detailsUiState = ingredientDetailsViewModel.ingredientDetailsFlow.collectAsState()

    LaunchedEffect(key1 = ingredientId) {
        ingredientDetailsViewModel.loadIngredientDetails(ingredientId)
    }

    when (detailsUiState.value) {
        IngredientDetailsUiState.Loading -> {
            CircularProgressIndicator()
        }

        is IngredientDetailsUiState.Success -> {
            val ingredient = (detailsUiState.value as IngredientDetailsUiState.Success).ingredient
            IngredientDetailsContent(
                ingredient = ingredient,
                onBackButtonClick = onBackButtonClick,
                onDeleteClick = {
                    ingredientDetailsViewModel.deleteIngredient(it)
                    onBackButtonClick()
                },
                onEditClick = {
                    ingredientDetailsViewModel.updateIngredient(it)
                }
            )
        }

        IngredientDetailsUiState.Error -> {

        }
    }
}

@Composable
fun IngredientDetailsContent(
    ingredient: Ingredient,
    onBackButtonClick: () -> Unit,
    onDeleteClick: (Int) -> Unit,
    onEditClick: (Ingredient) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3 / 4f),
                    model = ingredient.image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                0.0f to Color.Transparent,
                                1.0f to Background
                            )
                        )
                        .align(Alignment.BottomCenter)
                        .padding(top = 64.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, start = 20.dp),
                        text = ingredient.name,
                        textAlign = TextAlign.Left,
                        fontSize = 24.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = MainText
                    )

                }

                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 32.dp, start = 20.dp),
                    onClick = onBackButtonClick,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = Background,
                        contentColor = MainAccent
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowLeft,
                        contentDescription = null
                    )
                }

                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 32.dp, end = 20.dp),
                    onClick = { onDeleteClick(ingredient.id) },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = Background,
                        contentColor = MainAccent
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null
                    )
                }
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 20.dp),
                text = stringResource(
                    id = R.string.details_expires_on,
                    ingredient.expireDate.date,
                    ingredient.expireDate.month,
                    ingredient.expireDate.year
                ),
                textAlign = TextAlign.Left,
                fontSize = 20.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = MainText,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 20.dp),
                text = stringResource(
                    id = R.string.details_quantity,
                    ingredient.quantity,
                    ingredient.unit
                ),
                textAlign = TextAlign.Left,
                fontSize = 20.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = MainText,
            )
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .fillMaxWidth(.7f),
            onClick = { showBottomSheet = true },
            containerColor = MainAccent,
            contentColor = Background,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.details_edit),
                fontSize = 18.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

    if (showBottomSheet) {
        EditBottomSheet(
            ingredient = ingredient,
            onDismissRequest = { showBottomSheet = false },
            onEditClick = onEditClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBottomSheet(
    ingredient: Ingredient,
    onDismissRequest: () -> Unit,
    onEditClick: (Ingredient) -> Unit
) {
    var amountEntered by remember { mutableStateOf("") }

    var enteredAmount by remember { mutableStateOf(false) }

    var amountError by remember { mutableStateOf(false) }

    fun validate(text: String) {
        amountError = if (text.isEmpty()) {
            true
        } else if (text.contains(".") || text.contains(",") || text.contains("-")) {
            true
        } else text.toInt() <= 0
    }

    ModalBottomSheet(
        modifier = Modifier.imePadding(),
        onDismissRequest = onDismissRequest,
        containerColor = Background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.details_edit_title, ingredient.name),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = MainText,
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(.5f),
                    value = amountEntered,
                    onValueChange = {
                        amountEntered = it
                        enteredAmount = it.isNotEmpty()
                    },
                    textStyle = TextStyle(
                        fontFamily = Poppins,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MainText
                    ),
                    shape = RoundedCornerShape(8.dp),
                    placeholder = {
                        Text(
                            modifier = Modifier.alpha(.6f),
                            text = "00",
                            textAlign = TextAlign.Left,
                            fontSize = 20.sp,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.SemiBold,
                            color = SecondaryText
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Background,
                        unfocusedContainerColor = Background,
                        errorContainerColor = Background,
                        focusedIndicatorColor = MainAccent,
                        unfocusedIndicatorColor = LightAccent,
                        errorIndicatorColor = Error,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    isError = amountError,
                    supportingText = {
                        if (amountError) {
                            Text(
                                text = stringResource(id = R.string.fridge_add_amount_error),
                                textAlign = TextAlign.Left,
                                fontSize = 12.sp,
                                fontFamily = Poppins,
                                fontWeight = FontWeight.SemiBold,
                                color = Error
                            )
                        }
                    },
                    keyboardActions = KeyboardActions {
                        validate(amountEntered)
                    },
                )

                Text(
                    text = ingredient.unit,
                    textAlign = TextAlign.Left,
                    fontSize = 20.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = MainText,
                )
            }

            FloatingActionButton(
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth(.7f),
                onClick = {
                    onEditClick(ingredient.copy(quantity = amountEntered.toInt()))
                    onDismissRequest()
                },
                containerColor = MainAccent,
                contentColor = Background,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.generic_save),
                    fontSize = 18.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
