package com.mready.myapplication.ui.onboarding.forgotpass

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mready.myapplication.R
import com.mready.myapplication.ui.theme.Error
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.SecondaryText
import com.mready.myapplication.ui.utils.FridgeBuddyButton
import com.mready.myapplication.ui.utils.FridgeBuddyTextField

@Composable
fun ForgotPassScreen(
    onBackClick: () -> Unit,
    onSendClick: () -> Unit,
    email: String
) {
    val forgotPassViewModel: ForgotPassViewModel = hiltViewModel()

    val context = LocalContext.current

    var email by rememberSaveable { mutableStateOf(email) }

    var emailError by rememberSaveable { mutableStateOf(false) }

    fun validate(text: String) {
        emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()

    }

    BackHandler {
        onBackClick()
    }

    val resetSuccess = stringResource(id = R.string.onboarding_reset_pass_success)
    val resetFailure = stringResource(id = R.string.onboarding_reset_pass_failure)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(top = 20.dp, end = 32.dp),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 16.dp),
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(40.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = {
                            onBackClick()
                        }
                    ),
                imageVector = Icons.Outlined.KeyboardArrowLeft,
                contentDescription = null,
                tint = MainText
            )

            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(id = R.string.onboarding_reset_pass),
                textAlign = TextAlign.Left,
                fontSize = 24.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = MainText
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 56.dp, start = 32.dp),
            text = stringResource(id = R.string.forgot_pass_email),
            textAlign = TextAlign.Left,
            fontSize = 16.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = SecondaryText
        )

        FridgeBuddyTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp),
            value = email,
            onValueChange = { email = it },
            placeholder = stringResource(id = R.string.onboarding_email_example),
            isError = emailError,
            keyboardActions = KeyboardActions {
                validate(email)
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            supportingText = {
                if (emailError) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        text = stringResource(id = R.string.onboarding_email_error),
                        textAlign = TextAlign.Left,
                        fontSize = 12.sp,
                        fontFamily = Poppins,
                        color = Error
                    )
                }
            },
        )

        FridgeBuddyButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 32.dp),
            text = stringResource(id = R.string.onboarding_send_reset_email),
            onClick = {
                validate(email)
                if (!emailError) {
                    forgotPassViewModel.resetPassword(email, onSuccess = {
                        Toast.makeText(context, resetSuccess, Toast.LENGTH_SHORT).show()
                    }, onFailure = {
                        Toast.makeText(context, resetFailure, Toast.LENGTH_SHORT).show()
                    })
                    onSendClick()
                }
            }
        )
    }
}