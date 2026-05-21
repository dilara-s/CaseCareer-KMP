package ru.kpfu.itis.feature.profile.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.core.viewmodel.CommonViewModel
import ru.kpfu.itis.feature.profile.domain.usecase.GetProfileUseCase
import ru.kpfu.itis.feature.profile.domain.usecase.LogoutUseCase

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : CommonViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileEffect>(extraBufferCapacity = 1)
    val effect: SharedFlow<ProfileEffect> = _effect.asSharedFlow()

    init {
        onEvent(ProfileEvent.Load)
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.Load -> loadProfile()
            ProfileEvent.ShowLogoutDialog -> _state.update { it.copy(showLogoutDialog = true) }
            ProfileEvent.DismissLogoutDialog -> _state.update { it.copy(showLogoutDialog = false) }
            ProfileEvent.ConfirmLogout -> logout()
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getProfileUseCase()
                .onSuccess { profile ->
                    _state.update { it.copy(isLoading = false, profile = profile) }
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _effect.emit(ProfileEffect.NavigateToAuth)
        }
    }
}
