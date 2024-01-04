package com.mready.myapplication.ui.fridge.scan

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.mready.myapplication.R
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.Error
import com.mready.myapplication.ui.theme.LightAccent
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins

@Composable
fun ScanResultScreen(
    viewModel: ScanViewModel,
    text: String,
    onBack: () -> Unit,
    onConfirmClick: (String) -> Unit
) {
    BackHandler {
        onBack()
    }

    var scannedTextFieldValue by remember {
        mutableStateOf(text)
    }

    val customTextSelectionColors = TextSelectionColors(
        handleColor = MainAccent,
        backgroundColor = LightAccent
    )

    val capturedImage by viewModel.capturedImageFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(bottom = 16.dp, start = 20.dp, end = 20.dp)
    ) {
        Text(
            modifier = Modifier.padding(top = 32.dp),
            text = stringResource(id = R.string.scanned_screen_title),
            fontSize = 24.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold
        )

        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(.9f)
                    .align(Alignment.CenterHorizontally),
                value = scannedTextFieldValue,
                onValueChange = {
                    scannedTextFieldValue = it
                },
                textStyle = TextStyle(
                    fontFamily = Poppins,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MainText
                ),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Background,
                    unfocusedContainerColor = Background,
                    errorContainerColor = Background,
                    focusedIndicatorColor = MainAccent,
                    unfocusedIndicatorColor = LightAccent,
                    errorIndicatorColor = Error,
                    cursorColor = MainText,
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
            )
        }

        Image(
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth(.9f)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(8.dp)),
            painter = rememberAsyncImagePainter(model = capturedImage),
            contentDescription = null
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .align(Alignment.CenterHorizontally),
            onClick = {
                viewModel.updateTitle(scannedTextFieldValue)
                onConfirmClick(scannedTextFieldValue)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MainAccent,
                contentColor = Background
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                modifier = Modifier.padding(vertical = 6.dp),
                text = stringResource(id = R.string.scanned_screen_button),
                fontSize = 18.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}