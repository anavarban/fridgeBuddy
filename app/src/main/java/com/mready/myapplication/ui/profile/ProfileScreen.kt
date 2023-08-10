package com.mready.myapplication.ui.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mready.myapplication.R
import com.mready.myapplication.ui.onboarding.OnboardingViewModel
import com.mready.myapplication.ui.theme.MainAccent
import com.mready.myapplication.ui.theme.MainText
import com.mready.myapplication.ui.theme.Poppins
import com.mready.myapplication.ui.utils.getProfilePicture
import kotlin.random.Random

@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit,
    onboardingViewModel: OnboardingViewModel,
) {

    val storage = Firebase.storage

    val profilePictureIdx = Random.nextInt(from = 0, until = 6)

    val storageRef = storage.reference.child("profile/profile_$profilePictureIdx.png")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        Row(
            modifier = Modifier
                .padding(start = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .border(1.dp, MainAccent, CircleShape)
                    .clip(CircleShape)
                    .size(120.dp),
                model = getProfilePicture(storageRef),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Column {
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = onboardingViewModel.currentUser?.displayName ?: "",
                    fontSize = 20.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = MainText
                )
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = onboardingViewModel.currentUser?.email ?: "",
                    fontSize = 20.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = MainText
                )
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .padding(top = 40.dp)
                .align(CenterHorizontally),
            onClick = {
                onboardingViewModel.logout()
                onLogoutClick()
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