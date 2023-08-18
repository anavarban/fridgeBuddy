package com.mready.myapplication.ui.fridge.addingredient

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mready.myapplication.R
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.Error
import com.mready.myapplication.ui.theme.LightAccent
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.SecondaryText
import com.mready.myapplication.ui.theme.Surface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAmount(
    user: String,
    ingredientName: String,
    onNextClick: (String, String, String, Int) -> Unit
) {
    val measurementUnits = listOf("grams", "milliliters", "tsp", "pieces")

    var selectedUnit by remember {
        mutableStateOf("")
    }
    var expandedMenu by remember {
        mutableStateOf(false)
    }

    var amountEntered by remember {
        mutableStateOf("")
    }

    var enteredAmount by remember {
        mutableStateOf(false)
    }

    var pickedUnit by remember {
        mutableStateOf(false)
    }

    var amountError: Boolean by remember {
        mutableStateOf(false)
    }

    fun validate(text: String) {
        amountError = if (text.isEmpty()) {
            true
        } else if (text.contains(".") || text.contains(",") || text.contains("-")) {
            true
        } else text.toInt() <= 0
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(top = 64.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.TopCenter),
            text = stringResource(id = R.string.fridge_add_amount, ingredientName),
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = MainText
        )

        Row(
            modifier = Modifier
                .padding(top = 80.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(end = 16.dp)
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

            ExposedDropdownMenuBox(
                modifier = Modifier,
                expanded = expandedMenu,
                onExpandedChange = { expandedMenu = !expandedMenu }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .menuAnchor(),
                    value = selectedUnit,
                    onValueChange = {},
                    readOnly = true,
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
                            text = stringResource(id = R.string.fridge_add_amount_unit),
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
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMenu)
                    },
                    isError = amountError,
                    supportingText = {
                        if (amountError) {
                            Text(
                                text = "secret",
                                textAlign = TextAlign.Left,
                                fontSize = 12.sp,
                                fontFamily = Poppins,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Transparent
                            )
                        }
                    }
                )

                ExposedDropdownMenu(
                    modifier = Modifier.background(Surface),
                    expanded = expandedMenu,
                    onDismissRequest = { expandedMenu = false }
                ) {
                    measurementUnits.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = it,
                                    textAlign = TextAlign.Left,
                                    fontSize = 14.sp,
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SecondaryText
                                )
                            },
                            onClick = {
                                selectedUnit = it
                                expandedMenu = false
                                pickedUnit = true
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = MainText,
                                disabledTextColor = MainText
                            )
                        )
                    }
                }
            }

        }
        Log.d("AddAmount", "pickedUnit: $pickedUnit, enteredAmount: $enteredAmount")
        if (pickedUnit && enteredAmount) {
            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(.8f)
                    .padding(bottom = 32.dp),
                onClick = {
                    validate(amountEntered)
                    Log.d("AddAmount", "amountError: $amountError")
                    if (!amountError) {
                        onNextClick(user, ingredientName, selectedUnit, amountEntered.toInt())
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MainAccent
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.generic_next),
                    fontSize = 20.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}