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
class SignUpViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    private val _signUpFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signUpFlow: StateFlow<Resource<FirebaseUser>?> = _signUpFlow

    val currentUser = authRepository.currentUser

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _signUpFlow.update { Resource.Loading }
            val result = authRepository.signUp(name, email, password)
            _signUpFlow.update { result }
        }
    }

    fun googleSignIn(credential: AuthCredential) {
        viewModelScope.launch {
            _signUpFlow.update { Resource.Loading }
            val result = authRepository.googleSignIn(credential = credential)
            _signUpFlow.update { result }
        }
    }

}