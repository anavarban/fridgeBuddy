package com.mready.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mready.myapplication.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(authRepository: AuthRepository) : ViewModel() {

    private var _startScreenFlow = MutableStateFlow<StartScreenState?>(null)
    val startScreenFlow: StateFlow<StartScreenState?> = _startScreenFlow

    val currentUser = authRepository.currentUser

    init {
        viewModelScope.launch {
            authRepository.currentUser.collect {
                if (authRepository.currentUser.value != null) {
                    _startScreenFlow.update {
                        StartScreenState.Dashboard
                    }
                } else {
                    _startScreenFlow.update { StartScreenState.Onboarding }
                }
            }
        }
    }

}

sealed class StartScreenState {
    object Onboarding : StartScreenState()
    object Dashboard : StartScreenState()
}