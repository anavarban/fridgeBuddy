package com.mready.myapplication.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
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

@Composable
fun LoginScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(top = 20.dp, start = 32.dp, end = 32.dp),

        ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            text = stringResource(id = R.string.onboarding_login),
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = MainText
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            text = stringResource(id = R.string.onboarding_email),
            textAlign = TextAlign.Left,
            fontSize = 16.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = SecondaryText
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = TextFieldValue(""),
            onValueChange = {/* TODO */ },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(.6f),
                    text = stringResource(id = R.string.onboarding_email_example),
                    textAlign = TextAlign.Left,
                    fontSize = 14.sp,
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
            )
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            text = stringResource(id = R.string.onboarding_password),
            textAlign = TextAlign.Left,
            fontSize = 16.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = SecondaryText
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = TextFieldValue(""),
            onValueChange = {/* TODO */ },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(.6f),
                    text = stringResource(id = R.string.onboarding_pass_placeholder),
                    textAlign = TextAlign.Left,
                    fontSize = 14.sp,
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
            )
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { /*TODO*/ }
                ),
            text = stringResource(id = R.string.onboarding_forgot_pass),
            textAlign = TextAlign.Right,
            fontSize = 14.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = SecondaryText
        )

        Spacer(
            modifier = Modifier.height(80.dp)
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            onClick = {/*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                containerColor = MainAccent
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.onboarding_login),
                fontSize = 20.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
        ) {
            Divider(
                modifier = Modifier.padding(top = 8.dp),
                thickness = 1.dp,
                color = LightAccent
            )
            Text(
                modifier = Modifier
                    .background(Background)
                    .align(Alignment.Center)
                    .padding(start = 4.dp, end = 4.dp),
                text = stringResource(id = R.string.generic_or),
                fontSize = 12.sp,
                fontFamily = Poppins,
                color = LightAccent
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            text = stringResource(id = R.string.onboarding_continue_options),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontFamily = Poppins,
            color = SecondaryText
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center,

        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(32.dp),
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = null,
                tint = MainAccent
            )
            Icon(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(32.dp),
                painter = painterResource(id = R.drawable.ic_facebook_f),
                contentDescription = null,
                tint = MainAccent
            )
            Icon(
                modifier = Modifier
                    .size(32.dp),
                painter = painterResource(id = R.drawable.ic_apple_logo),
                contentDescription = null,
                tint = MainAccent
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.onboarding_sign_up_prompt),
                fontSize = 16.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = SecondaryText
            )
            Text(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { /*TODO*/ }
                    ) ,
                text = stringResource(id = R.string.onboarding_sign_up),
                fontSize = 16.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = MainAccent
            )
        }
    }

}