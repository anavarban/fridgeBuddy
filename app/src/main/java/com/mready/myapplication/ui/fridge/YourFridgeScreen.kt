package com.mready.myapplication.ui.fridge

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mready.myapplication.R
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.Poppins

@Composable
fun YourFridgeScreen() {

    Button(
        modifier = Modifier.fillMaxWidth(.7f),
        onClick = { /* TODO navigate to add */ },
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