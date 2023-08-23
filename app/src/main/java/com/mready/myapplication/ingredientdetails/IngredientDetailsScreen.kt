package com.mready.myapplication.ingredientdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mready.myapplication.R
import com.mready.myapplication.models.Date
import com.mready.myapplication.models.Ingredient
import com.mready.myapplication.ui.dashboard.RecipeItem
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.DarkAccent
import com.mready.myapplication.ui.theme.Error
import com.mready.myapplication.ui.theme.LightAccent
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.SecondaryText
import java.util.Calendar

@Composable
fun IngredientDetailsScreen(
    ingredientId: Int,
    onBackButtonClick: () -> Unit,
    onRecipeClick: (String, Int) -> Unit
) {
    val ingredientDetailsViewModel: IngredientDetailsViewModel = hiltViewModel()

    val detailsUiState = ingredientDetailsViewModel.ingredientDetailsFlow.collectAsState()

    val ingredientRecipesState = ingredientDetailsViewModel.ingredientRecipesFlow.collectAsState()

    LaunchedEffect(key1 = ingredientId) {
        ingredientDetailsViewModel.loadIngredientDetails(ingredientId)
    }

    when (detailsUiState.value) {
        IngredientDetailsUiState.Loading -> {
            CircularProgressIndicator()
        }

        is IngredientDetailsUiState.Success -> {
            val ingredient = (detailsUiState.value as IngredientDetailsUiState.Success).ingredient
//            IngredientDetailsContent(
//                ingredient = ingredient,
//                onBackButtonClick = onBackButtonClick,
//                onDeleteClick = {
//                    ingredientDetailsViewModel.deleteIngredient(it)
//                    onBackButtonClick()
//                },
//                onEditClick = {
//                    ingredientDetailsViewModel.updateIngredient(it)
//                }
//            )
            LaunchedEffect(key1 = null){
                ingredientDetailsViewModel.getRecipesByIngredient(ingredient.name)
            }

            var showBottomSheet by remember { mutableStateOf(false) }

            var showPopUp by remember { mutableStateOf(false) }

            Box(modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
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


                    }

                    val dayFormatted = if (ingredient.expireDate.date < 10) {
                        "0${ingredient.expireDate.date}"
                    } else {
                        "${ingredient.expireDate.date}"
                    }

                    val monthFormatted = if (ingredient.expireDate.month < 10) {
                        "0${ingredient.expireDate.month}"
                    } else {
                        "${ingredient.expireDate.month}"
                    }

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 20.dp),
                        text = stringResource(
                            id = R.string.details_expires_on,
                            dayFormatted,
                            monthFormatted,
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

                    //todo add recommended recipes
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 20.dp),
                        text = stringResource(id = R.string.details_recommended_recipes, ingredient.name),
                        textAlign = TextAlign.Left,
                        fontSize = 20.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkAccent,
                    )


                    when (ingredientRecipesState.value) {
                        IngredientRecipesState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 32.dp),
                                color = MainAccent
                            )
                        }

                        is IngredientRecipesState.Success -> {
                            val recipes = (ingredientRecipesState.value as IngredientRecipesState.Success).recipes
                            Row (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.height(20.dp))
                                recipes.forEach {
                                    RecipeItem(
                                        modifier = Modifier
                                            .width(240.dp)
                                            .height(180.dp),
                                        recipe = it,
                                        baseIngredient = ingredient.name,
                                        onClick = { str -> onRecipeClick(str, recipes.indexOf(it)) }
                                    )
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }

                        IngredientRecipesState.Error -> TODO()
                    }

                    FloatingActionButton(
                        modifier = Modifier
                            .padding(top = 32.dp, bottom = 20.dp)
                            .fillMaxWidth(.7f)
                            .align(Alignment.CenterHorizontally),
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
                    onClick = { showPopUp = true },
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

            if (showBottomSheet) {
                EditBottomSheet(
                    ingredient = ingredient,
                    onDismissRequest = { showBottomSheet = false },
                    onEditClick = {
                        ingredientDetailsViewModel.updateIngredient(it)
                        showBottomSheet = false
                    }
                )
            }

            if (showPopUp) {
                DeletePopUp(
                    ingredientId = ingredient.id,
                    onDismissRequest = { showPopUp = false },
                    onDeleteClick = {
                        ingredientDetailsViewModel.deleteIngredient(it)
                        showPopUp = false
                        onBackButtonClick()
                    }
                )
            }
        }

        IngredientDetailsUiState.Error -> {
            //todo
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeletePopUp(
    ingredientId: Int,
    onDismissRequest: () -> Unit,
    onDeleteClick: (Int) -> Unit
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
                    onDeleteClick(ingredientId)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBottomSheet(
    ingredient: Ingredient,
    onDismissRequest: () -> Unit,
    onEditClick: (Ingredient) -> Unit
) {
    var amountEntered by remember { mutableStateOf("${ingredient.quantity}") }

    var amountError by remember { mutableStateOf(false) }

    fun validate(text: String) {
        amountError = if (text.isEmpty()) {
            true
        } else if (!text.all { it.isDigit() }) {
            true
        } else text.toInt() <= 0
    }

    val today = Calendar.getInstance()

    val expDate = Calendar.getInstance().apply {
        set(
            ingredient.expireDate.year,
            ingredient.expireDate.month - 1,
            ingredient.expireDate.date
        )
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = expDate.timeInMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return (utcTimeMillis >= today.timeInMillis)
            }
        },
        initialDisplayMode = DisplayMode.Input
    )

    ModalBottomSheet(
        modifier = Modifier,
        onDismissRequest = onDismissRequest,
        containerColor = Background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 20.dp, end = 20.dp),
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
                    },
                    textStyle = TextStyle(
                        fontFamily = Poppins,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MainText
                    ),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 1,
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

            DatePicker(
                modifier = Modifier.fillMaxWidth(),
                state = datePickerState,
                title = {},
                headline = {},
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    containerColor = Background,
                    titleContentColor = Background,
                    headlineContentColor = MainText,
                    weekdayContentColor = SecondaryText,
                    subheadContentColor = SecondaryText,
                    yearContentColor = SecondaryText,
                    currentYearContentColor = MainAccent,
                    selectedYearContentColor = Background,
                    selectedYearContainerColor = MainAccent,
                    dayContentColor = MainText,
                    disabledDayContentColor = SecondaryText,
                    selectedDayContentColor = Background,
                    disabledSelectedDayContentColor = MainText,
                    selectedDayContainerColor = MainAccent,
                    disabledSelectedDayContainerColor = MainAccent,
                    todayContentColor = MainText,
                    todayDateBorderColor = MainAccent,
                    dayInSelectionRangeContentColor = MainText,
                    dayInSelectionRangeContainerColor = MainAccent,
                    dateTextFieldColors = TextFieldDefaults.colors(
                        focusedContainerColor = Background,
                        unfocusedContainerColor = Background,
                        errorContainerColor = Background,
                        focusedIndicatorColor = MainAccent,
                        unfocusedIndicatorColor = LightAccent,
                        errorIndicatorColor = Error,
                        focusedLabelColor = MainAccent,
                        unfocusedLabelColor = LightAccent,
                        disabledLabelColor = SecondaryText,
                        errorLabelColor = Error,
                    ),
                ),
            )

            FloatingActionButton(
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth(.7f),
                onClick = {
                    validate(amountEntered)
                    if (!amountError) {
                        val calendar = Calendar.getInstance().apply {
                            timeInMillis = datePickerState.selectedDateMillis ?: 0
                        }
                        onEditClick(
                            ingredient.copy(
                                quantity = amountEntered.toInt(),
                                expireDate = Date(
                                    year = calendar.get(Calendar.YEAR),
                                    month = calendar.get(Calendar.MONTH) + 1,
                                    date = calendar.get(Calendar.DAY_OF_MONTH),
                                )
                            )
                        )
                        onDismissRequest()
                    }
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
