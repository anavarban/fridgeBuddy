package com.mready.myapplication.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.mready.myapplication.R
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.Poppins
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashEnded: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MainAccent)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            text = stringResource(id = R.string.app_name),
            textAlign = TextAlign.Center,
            fontFamily = Poppins,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }

    LaunchedEffect(key1 = null) {
        delay(1000)
        onSplashEnded()
    }
}