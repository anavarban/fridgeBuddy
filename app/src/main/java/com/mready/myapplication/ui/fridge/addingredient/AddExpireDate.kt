package com.mready.myapplication.ui.fridge.addingredient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mready.myapplication.R
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.SecondaryText
import com.mready.myapplication.ui.utils.FridgeBuddyButton
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpireDate(
    user: String,
    ingredientName: String,
    unit: String,
    amount: Int,
    onDateSelected: (Long) -> Unit,
    onDoneClick: () -> Unit,
) {
    val today = Calendar.getInstance().apply {
        set(
            get(Calendar.YEAR),
            get(Calendar.MONTH),
            get(Calendar.DAY_OF_MONTH) - 1,
            23,
            59,
            59
        )
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Calendar.getInstance().timeInMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return (utcTimeMillis >= today.timeInMillis)
            }
        }
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp),
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
            color = MainText,
            minLines = 2
        )

        DatePicker(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
            state = datePickerState,
            title = null,
            showModeToggle = false,
            dateFormatter = remember { DatePickerDefaults.dateFormatter() },
            colors = DatePickerDefaults.colors(
                containerColor = Background,
                titleContentColor = Background,
                headlineContentColor = MainAccent,
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
                ),
        )

        Spacer(modifier = Modifier.weight(1f))

        FridgeBuddyButton(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .padding(bottom = 32.dp),
            text = stringResource(id = R.string.generic_done),
            onClick = {
                onDateSelected(datePickerState.selectedDateMillis ?: 0)
                onDoneClick()
            },
        )
    }

}