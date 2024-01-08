package com.mready.myapplication.ui.onboarding.signup

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import com.mready.myapplication.ui.theme.Error
import com.mready.myapplication.ui.theme.LightAccent
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.theme.SecondaryText
import com.mready.myapplication.ui.utils.FridgeBuddyButton
import com.mready.myapplication.ui.utils.FridgeBuddyTextField
import com.mready.myapplication.ui.utils.LoadingAnimation
import com.mready.myapplication.ui.utils.clientId
import com.mready.myapplication.ui.utils.signUpFields

@Composable
fun SignUpScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    val context = LocalContext.current

    val signUpViewModel: SignUpViewModel = hiltViewModel()

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisibility by remember {
        mutableStateOf(false)
    }

    var name by remember {
        mutableStateOf("")
    }

    var nameError by remember {
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

    LaunchedEffect(key1 = signUpViewModel.signUpFlow) {
        signUpViewModel.signUpFlow.collect {
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
                    onSignUpClick()
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


    fun validate(text: String, type: signUpFields) {
        when (type) {
            signUpFields.NAME -> {
                nameError = text.length < 3 || !text.all { it.isLetterOrDigit() }
            }

            signUpFields.EMAIL -> {
                emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()
            }

            signUpFields.PASSWORD -> {
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
                signUpViewModel.googleSignIn(credential)
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
            text = stringResource(id = R.string.onboarding_sign_up),
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
            text = stringResource(id = R.string.onboarding_sign_up_name),
            textAlign = TextAlign.Left,
            fontSize = 16.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = SecondaryText
        )

        FridgeBuddyTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = name,
            onValueChange = { name = it },
            placeholder = stringResource(id = R.string.onboarding_name_placeholder),
            isError = nameError,
            keyboardActions = KeyboardActions {
                validate(name, signUpFields.NAME)
            },
            supportingText = {
                if (nameError) {
                    Text(
                        modifier = Modifier
                            .padding(top = 4.dp),
                        text = stringResource(id = R.string.onboarding_name_error),
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
            text = stringResource(id = R.string.onboarding_email),
            textAlign = TextAlign.Left,
            fontSize = 16.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            color = SecondaryText
        )

        FridgeBuddyTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = email,
            onValueChange = { email = it },
            placeholder = stringResource(id = R.string.onboarding_email_example),
            isError = emailError,
            keyboardActions = KeyboardActions {
                validate(email, signUpFields.EMAIL)
            },
            supportingText = {
                if (emailError) {
                    Text(
                        modifier = Modifier,
                        text = stringResource(id = R.string.onboarding_email_error),
                        textAlign = TextAlign.Left,
                        fontSize = 12.sp,
                        fontFamily = Poppins,
                        color = Error
                    )
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
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

        FridgeBuddyTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = password,
            onValueChange = { password = it },
            placeholder = stringResource(id = R.string.onboarding_pass_placeholder),
            isError = passwordError,
            keyboardActions = KeyboardActions {
                validate(password, signUpFields.PASSWORD)
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisibility) {
                androidx.compose.ui.text.input.VisualTransformation.None
            } else {
                androidx.compose.ui.text.input.PasswordVisualTransformation()
            },
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
            }
        )

        Spacer(modifier = Modifier.height(80.dp))

        FridgeBuddyButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            text = stringResource(id = R.string.onboarding_sign_up),
            onClick = {
                validate(name, signUpFields.NAME)
                validate(email, signUpFields.EMAIL)
                validate(password, signUpFields.PASSWORD)
                if (!nameError && !emailError && !passwordError) {
                    signUpViewModel.signUp(name, email, password)
                }
            },
        )

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
            text = stringResource(id = R.string.onboarding_sign_up_options),
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
                    .padding(end = 0.dp)
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
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.onboarding_login_prompt),
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
                        onClick = onLoginClick
                    ),
                text = stringResource(id = R.string.onboarding_login),
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