package com.mready.myapplication.ui.onboarding.forgotpass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mready.myapplication.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPassViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    fun resetPassword(email: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            authRepository.resetPassword(email, onSuccess = onSuccess, onFailure = onFailure)
        }
    }
}
