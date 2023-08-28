package com.mready.myapplication.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.mready.myapplication.auth.AuthRepository
import com.mready.myapplication.auth.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginFlow.update { Resource.Loading }
            val result = authRepository.login(email, password)
            _loginFlow.update { result }
        }
    }

    fun googleSignIn(credential: AuthCredential) {
        viewModelScope.launch {
            _loginFlow.update { Resource.Loading }
            val result = authRepository.googleSignIn(credential = credential)
            _loginFlow.update { result }
        }
    }

    fun resetPassword(email: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            authRepository.resetPassword(email, onSuccess = onSuccess, onFailure = onFailure)
        }
    }

}

sealed class LoginUiState {
    object Loading : LoginUiState()
    object Error : LoginUiState()
    object Success : LoginUiState()
}