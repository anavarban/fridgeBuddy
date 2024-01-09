package com.mready.myapplication.ui.fridge.scan

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.mready.myapplication.R
import com.mready.myapplication.ui.theme.LightAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.utils.FridgeBuddyButton
import com.mready.myapplication.ui.utils.FridgeBuddyTextField

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

    val capturedImage by viewModel.capturedImagePathFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 42.dp, start = 20.dp, end = 20.dp),
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(32.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = {
                            onBack()
                        }
                    ),
                imageVector = Icons.Outlined.KeyboardArrowLeft,
                contentDescription = null,
                tint = MainText
            )

            Text(
                modifier = Modifier,
                text = stringResource(id = R.string.scanned_screen_title),
                fontSize = 24.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = MainText
            )
        }

        FridgeBuddyTextField(
            modifier = Modifier
                .padding(top = 24.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth(.9f)
                .align(Alignment.CenterHorizontally),
            value = scannedTextFieldValue,
            onValueChange = {
                scannedTextFieldValue = it
            },
            maxLines = Int.MAX_VALUE,
            placeholder = stringResource(id = R.string.scanned_screen_placeholder),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
        )

        Image(
            modifier = Modifier
                .padding(top = 32.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth(.9f)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(8.dp)),
            painter = rememberAsyncImagePainter(model = capturedImage),
            contentDescription = null
        )

        Text(
            modifier = Modifier
                .padding(top = 16.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth(.9f)
                .align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.scanned_screen_info),
            fontSize = 16.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            color = LightAccent
        )

        Spacer(modifier = Modifier.weight(1f))

        FridgeBuddyButton(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.scanned_screen_button),
            onClick = {
                viewModel.updateTitle(scannedTextFieldValue)
                onConfirmClick(scannedTextFieldValue)
            },
        )
    }
}