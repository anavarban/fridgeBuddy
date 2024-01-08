package com.mready.myapplication.ui.utils

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.Poppins

@Composable
fun FridgeBuddyButton(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: Int = 20,
    containerColor: Color = MainAccent,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            fontSize = fontSize.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
        )
    }
}