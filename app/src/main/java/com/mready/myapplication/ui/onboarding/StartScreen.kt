package com.mready.myapplication.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mready.myapplication.R
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.SecondaryText

@Composable
fun StartScreen(
    onClick: () -> Unit
) {

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        
        AsyncImage(
            modifier = Modifier.fillMaxHeight(0.7f),
            model = "https://images.unsplash.com/photo-1493770348161-369560ae357d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80",
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        
        Text(
            text = stringResource(id = R.string.onboarding_start_title_message),
            fontSize = 24.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = MainText
        )

        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = stringResource(id = R.string.onboarding_start_intro_message),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontFamily = Poppins,
            color = SecondaryText
        )
        
        Button(
            modifier = Modifier.fillMaxWidth(.7f),
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MainAccent
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.onboarding_button_let_s_go),
                fontSize = 18.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

}