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
import ru.kpfu.itis.feature.profile.domain.usecase.UpdateProfileUseCase

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
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
            ProfileEvent.StartEdit -> startEdit()
            ProfileEvent.CancelEdit -> _state.update { it.copy(isEditing = false, error = null) }
            is ProfileEvent.UpdateFullName -> _state.update { it.copy(editFullName = event.value) }
            is ProfileEvent.UpdateSkills -> _state.update { it.copy(editSkills = event.value) }
            is ProfileEvent.UpdateContactInfo -> _state.update { it.copy(editContactInfo = event.value) }
            is ProfileEvent.UpdatePortfolioLink -> _state.update { it.copy(editPortfolioLink = event.value) }
            is ProfileEvent.UpdatePhone -> _state.update { it.copy(editPhone = event.value) }
            ProfileEvent.Save -> saveProfile()
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

    private fun startEdit() {
        val profile = _state.value.profile ?: return
        _state.update {
            it.copy(
                isEditing = true,
                editFullName = profile.fullName,
                editSkills = profile.skills,
                editContactInfo = profile.contactInfo,
                editPortfolioLink = profile.portfolioLink,
                editPhone = profile.phone,
                error = null
            )
        }
    }

    private fun saveProfile() {
        val s = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }
            updateProfileUseCase(
                fullName = s.editFullName.takeIf { it.isNotBlank() },
                skills = s.editSkills.takeIf { it.isNotBlank() },
                contactInfo = s.editContactInfo.takeIf { it.isNotBlank() },
                portfolioLink = s.editPortfolioLink.takeIf { it.isNotBlank() },
                phone = s.editPhone.takeIf { it.isNotBlank() }
            )
                .onSuccess { updated ->
                    _state.update { it.copy(isSaving = false, isEditing = false, profile = updated) }
                }
                .onFailure { e ->
                    _state.update { it.copy(isSaving = false, error = e.message) }
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
