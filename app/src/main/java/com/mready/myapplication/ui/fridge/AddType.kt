package com.mready.myapplication.ui.fridge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
fun AddType(
    onNextClick: (String) -> Unit,
) {

    val ingredientOptions = listOf(
        "Milk",
        "Chicken",
        "Yogurt",
        "Cheese",
        "Berries",
        "Chicken",
        "Yogurt",
        "Cheese",
        "Berries",
        "Chicken",
        "Yogurt",
        "Cheese",
        "Berries",
        "Chicken",
        "Yogurt",
        "Cheese",
        "Berries",
        "Chicken",
        "Yogurt",
        "Cheese",
        "Berries"
    )

    var selectedType by remember {
        mutableStateOf("")
    }
    var expandedMenu by remember {
        mutableStateOf(false)
    }

    var picked by remember {
        mutableStateOf(false)
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(top = 64.dp)
    ) {

        Text(
            modifier = Modifier.align(Alignment.TopCenter),
            text = "Add ingredient to your fridge",
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = MainText

        )

        ExposedDropdownMenuBox(
            modifier = Modifier
                .padding(top = 32.dp)
                .align(Alignment.TopCenter),
            expanded = expandedMenu,
            onExpandedChange = { expandedMenu = !expandedMenu }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth(.8f)
                    .menuAnchor(),
                value = selectedType,
                onValueChange = {
                },
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
                        text = "Pick ingredient",
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
                }
            )

            ExposedDropdownMenu(
                modifier = Modifier.background(Surface),
                expanded = expandedMenu,
                onDismissRequest = { expandedMenu = false }
            ) {
                ingredientOptions.forEach {
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
                            selectedType = it
                            expandedMenu = false
                            picked = true
                        },
                        colors = MenuDefaults.itemColors(
                            textColor = MainText,
                            disabledTextColor = MainText
                        )
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "1",
                textAlign = TextAlign.Center,
                fontSize = 28.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = MainAccent
            )
            Text(
                modifier = Modifier.padding(48.dp),
                text = "2",
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = LightAccent
            )
            Text(
                text = "3",
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = LightAccent
            )
        }

        if (picked) {
            Button(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(.8f)
                    .padding(top = 40.dp),
                onClick = { onNextClick(selectedType) },
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