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
class OnboardingViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _signUpFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signUpFlow: StateFlow<Resource<FirebaseUser>?> = _signUpFlow

    private val _googleFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val googleFlow: StateFlow<Resource<FirebaseUser>?> = _googleFlow

    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    init {
        if (authRepository.currentUser != null) {
            _loginFlow.update { Resource.Success(authRepository.currentUser!!) }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginFlow.update { Resource.Loading }
            val result = authRepository.login(email, password)
            _loginFlow.update { result }
        }
    }

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _signUpFlow.update { Resource.Loading }
            val result = authRepository.signUp(name, email, password)
            _signUpFlow.update { result }
        }
    }

    fun googleSignIn(credential: AuthCredential) {
        viewModelScope.launch {
            _googleFlow.update { Resource.Loading }
            val result = authRepository.googleSignIn(credential = credential)
            _googleFlow.update { result }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _loginFlow.update { null }
            _signUpFlow.update { null }
            _googleFlow.update { null }
        }
    }

}