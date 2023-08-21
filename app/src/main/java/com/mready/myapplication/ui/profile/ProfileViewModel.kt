package com.mready.myapplication.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mready.myapplication.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _profileUiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val profileUiState: StateFlow<ProfileUiState> = _profileUiState

    private var _profileUiEvent = MutableStateFlow<ProfileUiEvent>(ProfileUiEvent.LoggedOut)
    val profileUiEvent: StateFlow<ProfileUiEvent> = _profileUiEvent

    val user = authRepository.currentUser.value

    init {
        if (user == null) {
            _profileUiState.update { ProfileUiState.Error }
            _profileUiEvent.update { ProfileUiEvent.LoggedOut }
        } else {
            _profileUiState.update { ProfileUiState.Success }
            _profileUiEvent.update { ProfileUiEvent.LoggedIn }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _profileUiEvent.update { ProfileUiEvent.LoggedOut }
        }
    }

}

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    object Success : ProfileUiState()
    object Error : ProfileUiState()
}

sealed class ProfileUiEvent {
    object LoggedOut : ProfileUiEvent()
    object LoggedIn : ProfileUiEvent()
}