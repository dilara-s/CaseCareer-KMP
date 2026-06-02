package ru.kpfu.itis.feature.profile.presentation

import ru.kpfu.itis.feature.profile.domain.model.Profile

data class ProfileState(
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val error: String? = null,
    val showLogoutDialog: Boolean = false,
    val showDeleteAccountDialog: Boolean = false,
)
