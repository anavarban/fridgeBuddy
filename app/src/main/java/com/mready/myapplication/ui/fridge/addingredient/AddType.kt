package com.mready.myapplication.ui.fridge.addingredient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mready.myapplication.R
import com.mready.myapplication.ui.theme.LightAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.SecondaryText
import com.mready.myapplication.ui.theme.Surface
import com.mready.myapplication.ui.utils.FridgeBuddyButton
import com.mready.myapplication.ui.utils.FridgeBuddyTextField
import com.mready.myapplication.ui.utils.ingredientToUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddType(
    user: String,
    startingType: String = "",
    onNextClick: (String, String) -> Unit,
    onScanClick: () -> Unit
) {

    val ingredientOptions = ingredientToUrl.keys.toList()

    var selectedType by remember {
        mutableStateOf(startingType)
    }
    var expandedMenu by rememberSaveable {
        mutableStateOf(false)
    }

    var picked by remember {
        if (startingType.isNotEmpty()) {
            mutableStateOf(true)
        } else {
            mutableStateOf(false)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(top = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            modifier = Modifier,
            text = stringResource(id = R.string.fridge_add_ingredient),
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = MainText

        )

        ExposedDropdownMenuBox(
            modifier = Modifier
                .padding(top = 32.dp),
            expanded = expandedMenu,
            onExpandedChange = { expandedMenu = !expandedMenu }
        ) {
            FridgeBuddyTextField(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth(.8f)
                    .menuAnchor(),
                value = selectedType,
                onValueChange = {},
                readOnly = true,
                placeholder = stringResource(id = R.string.fridge_pick_ingredient),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMenu)
                }
            )

            ExposedDropdownMenu(
                modifier = Modifier.background(Surface),
                expanded = expandedMenu,
                onDismissRequest = { expandedMenu = false }
            ) {
                ingredientOptions.sorted().forEach {
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

        Text(
            text = stringResource(id = R.string.generic_or),
            modifier = Modifier
                .padding(top = 20.dp),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            color = LightAccent
        )

        FridgeBuddyButton (
            modifier = Modifier
                .fillMaxWidth(.8f)
                .padding(top = 20.dp),
            text = stringResource(id = R.string.fridge_go_to_scan),
            onClick = { onScanClick() },
        )

        Spacer(modifier = Modifier.weight(1f))

        if (picked) {
            FridgeBuddyButton(
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .padding(bottom = 32.dp),
                text = stringResource(id = R.string.generic_next),
                onClick = { onNextClick(user, selectedType) },
            )
        }
    }
}