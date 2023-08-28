package com.mready.myapplication.ui.onboarding

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.mready.myapplication.R
import com.mready.myapplication.auth.Resource
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.Background40
import com.mready.myapplication.ui.theme.Error
import com.mready.myapplication.ui.theme.LightAccent
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.SecondaryText
import com.mready.myapplication.ui.utils.LoadingAnimation
import com.mready.myapplication.ui.utils.LoginFields
import com.mready.myapplication.ui.utils.clientId

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    val context = LocalContext.current

    val loginViewModel: LoginViewModel = hiltViewModel()

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisibility by remember {
        mutableStateOf(false)
    }

    var emailError by remember {
        mutableStateOf(false)
    }

    var passwordError by remember {
        mutableStateOf(false)
    }

    val error = stringResource(id = R.string.generic_error)

    var isLoading by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = loginViewModel.loginFlow) {
        loginViewModel.loginFlow.collect {
            when (it) {
                is Resource.Error -> {
                    isLoading = false
                    Toast.makeText(
                        context,
                        it.exception.message ?: error,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Resource.Success -> {
                    isLoading = false
                    onLoginClick()
                }

                is Resource.Loading -> {
                    isLoading = true
                }

                else -> {
                    isLoading = false
                }
            }
        }
    }

    fun validate(text: String, type: LoginFields) {
        when (type) {
            LoginFields.EMAIL -> {
                emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()
            }

            LoginFields.PASSWORD -> {
                passwordError = text.length < 8 || !text.all { it.isLetterOrDigit() }
            }
        }
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val result = account.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(result?.idToken, null)
                loginViewModel.googleSignIn(credential)
            } catch (e: ApiException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }

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
            value = email,
            onValueChange = { email = it },
            textStyle = TextStyle(
                fontFamily = Poppins,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MainText
            ),
            maxLines = 1,
            placeholder = {
                Text(
                    modifier = Modifier
                        .alpha(.6f)
                        .padding(top = 4.dp),
                    text = stringResource(id = R.string.onboarding_email_example),
                    textAlign = TextAlign.Left,
                    fontSize = 16.sp,
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
            ),
            isError = emailError,
            keyboardActions = KeyboardActions {
                validate(email, LoginFields.EMAIL)
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
            value = password,
            onValueChange = { password = it },
            textStyle = TextStyle(
                fontFamily = Poppins,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MainText
            ),
            maxLines = 1,
            placeholder = {
                Text(
                    modifier = Modifier
                        .alpha(.6f)
                        .padding(top = 4.dp),
                    text = stringResource(id = R.string.onboarding_pass_placeholder),
                    textAlign = TextAlign.Left,
                    fontSize = 16.sp,
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
            ),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Icon(
                    modifier = Modifier
                        .size(44.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { passwordVisibility = !passwordVisibility }
                        )
                        .padding(8.dp),
                    painter = if (passwordVisibility)
                        painterResource(id = R.drawable.ic_visibility_off)
                    else
                        painterResource(id = R.drawable.ic_visibility),
                    contentDescription = null,
                    tint = if (!passwordError) MainAccent else Error
                )
            },
            isError = passwordError,
            keyboardActions = KeyboardActions {
                validate(password, LoginFields.PASSWORD)
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            supportingText = {
                if (passwordError) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        text = stringResource(id = R.string.onboarding_pass_error),
                        textAlign = TextAlign.Left,
                        fontSize = 12.sp,
                        fontFamily = Poppins,
                        color = Error
                    )
                }
            },
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
            onClick = {
                validate(email, LoginFields.EMAIL)
                validate(password, LoginFields.PASSWORD)
                if (!emailError && !passwordError) {
                    loginViewModel.login(email, password)
                }
            },
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
                    .size(32.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            val gso = GoogleSignInOptions
                                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestEmail()
                                .requestIdToken(clientId)
                                .build()

                            val googleSignInClient = GoogleSignIn.getClient(context, gso)

                            launcher.launch(googleSignInClient.signInIntent)
                        }
                    ),
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
                painter = painterResource(id = R.drawable.ic_instagram),
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
                        onClick = onSignUpClick
                    ),
                text = stringResource(id = R.string.onboarding_sign_up),
                fontSize = 16.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = MainAccent
            )
        }


    }

    if (isLoading) {
        LoadingAnimation()
    }

}