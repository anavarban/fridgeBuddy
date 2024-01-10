package com.mready.myapplication.ui.fridge.addingredient

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mready.myapplication.R
import com.mready.myapplication.ui.theme.Error
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.SecondaryText
import com.mready.myapplication.ui.theme.Surface
import com.mready.myapplication.ui.utils.FridgeBuddyButton
import com.mready.myapplication.ui.utils.FridgeBuddyTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAmount(
    user: String,
    ingredientName: String,
    onNextClick: (String, String, String, Int) -> Unit
) {
    val measurementUnits = listOf("grams", "milliliters", "pieces")

    var selectedUnit by remember {
        mutableStateOf("")
    }
    var expandedMenu by rememberSaveable {
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
        } else if (text.length > 6) {
            true
        } else if (!text.all { it.isDigit() }) {
            true
        } else text.toInt() <= 0
    }

    Column(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier,
            text = stringResource(id = R.string.fridge_add_amount, ingredientName),
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = MainText
        )

        Row(
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FridgeBuddyTextField(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .fillMaxWidth(.5f),
                value = amountEntered,
                onValueChange = {
                    amountEntered = it
                    enteredAmount = it.isNotEmpty()
                },
                placeholder = "00",
                isError = amountError,
                keyboardActions = KeyboardActions {
                    validate(amountEntered)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
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
                }
            )

            ExposedDropdownMenuBox(
                modifier = Modifier,
                expanded = expandedMenu,
                onExpandedChange = { expandedMenu = !expandedMenu }
            ) {
                FridgeBuddyTextField(
                    modifier = Modifier
                        .menuAnchor(),
                    value = selectedUnit,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = stringResource(id = R.string.fridge_add_amount_unit),
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

        Spacer(modifier = Modifier.weight(1f))

        AnimatedVisibility (
            visible = pickedUnit && enteredAmount,
            enter = fadeIn(tween(500)),
            exit = fadeOut(tween(500))
        ) {
            FridgeBuddyButton(
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .padding(bottom = 32.dp),
                text = stringResource(id = R.string.generic_next),
                onClick = {
                    validate(amountEntered)
                    if (!amountError) {
                        onNextClick(user, ingredientName, selectedUnit, amountEntered.toInt())
                    }
                }
            )
        }
    }
}