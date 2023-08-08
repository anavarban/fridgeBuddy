package com.mready.myapplication.ui.fridge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mready.myapplication.R
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.LightAccent
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.SecondaryText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpireDate(
    ingredientName: String
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = 1578096000000)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(top = 64.dp)
    ) {

        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier,
                text = "Select the expiration date for $ingredientName",
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = MainText

            )

            DatePicker(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                state = datePickerState,
                title = {},
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
                    disabledSelectedDayContentColor = MainAccent,
                    selectedDayContainerColor = MainAccent,
                    disabledSelectedDayContainerColor = MainAccent,
                    todayContentColor = MainText,
                    todayDateBorderColor = MainAccent,
                    dayInSelectionRangeContentColor = MainText,
                    dayInSelectionRangeContainerColor = MainAccent,

                )
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth(.8f),
                onClick = { /* TODO navigate */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MainAccent
                ),
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
                fontSize = 24.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = LightAccent
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
                fontSize = 28.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = MainAccent
            )
        }
    }
}