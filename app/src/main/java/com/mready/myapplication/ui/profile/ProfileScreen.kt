package com.mready.myapplication.ui.profile

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mready.myapplication.R
import com.mready.myapplication.ui.theme.Background
import com.mready.myapplication.ui.theme.Error
import com.mready.myapplication.ui.theme.LightAccent
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.utils.LoadingAnimation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val profileViewModel: ProfileViewModel = hiltViewModel()

    val profileState = profileViewModel.profileUiState.collectAsState()

    LaunchedEffect(key1 = profileViewModel.profileUiEvent) {
        profileViewModel.profileUiEvent.collect { event ->
            when (event) {
                is ProfileUiEvent.LoggedOut -> {
                    onLogoutClick()
                }
                else -> {
                    // Do nothing
                }
            }
        }
    }

    when (profileState.value) {
        ProfileUiState.Loading -> {
            LoadingAnimation()
        }

        ProfileUiState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .padding(top = 20.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Icon(
                        modifier = Modifier
                            .padding(start = 20.dp, end = 8.dp)
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
                        tint = MainAccent
                    )

                    Text(
                        modifier = Modifier,
                        text = stringResource(id = R.string.profile_title),
                        fontSize = 28.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = MainAccent
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = CenterHorizontally
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .border(1.dp, MainAccent, CircleShape)
                            .clip(CircleShape)
                            .size(120.dp),
                        model = profileViewModel.user?.photoUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(.8f),
                        value = TextFieldValue(profileViewModel.user?.displayName ?: ""),
                        onValueChange = {},
                        label = {
                            Text(
                                text = stringResource(id = R.string.profile_name),
                                fontSize = 12.sp,
                                fontFamily = Poppins,
                                fontWeight = FontWeight.SemiBold,
                                color = MainAccent
                            )
                        },
                        readOnly = true,
                        enabled = false,
                        colors = TextFieldDefaults.colors(
                            disabledContainerColor = Background,
                            disabledIndicatorColor = MainAccent
                        ),
                        textStyle = TextStyle(
                            fontFamily = Poppins,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MainText
                        ),
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(.8f),
                        value = TextFieldValue(profileViewModel.user?.email ?: ""),
                        onValueChange = {},
                        label = {
                            Text(
                                text = stringResource(id = R.string.profile_email),
                                fontSize = 12.sp,
                                fontFamily = Poppins,
                                fontWeight = FontWeight.SemiBold,
                                color = MainAccent
                            )
                        },
                        readOnly = true,
                        enabled = false,
                        colors = TextFieldDefaults.colors(
                            disabledContainerColor = Background,
                            disabledIndicatorColor = MainAccent
                        ),
                        textStyle = TextStyle(
                            fontFamily = Poppins,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MainText
                        ),
                    )
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth(.8f)
                        .padding(top = 40.dp)
                        .align(CenterHorizontally),
                    onClick = {
                        profileViewModel.logout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MainAccent
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.onboarding_logout),
                        fontSize = 20.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }

        ProfileUiState.Error -> {
            Text(
                text = stringResource(id = R.string.generic_error),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = MainText
            )
        }
    }


}