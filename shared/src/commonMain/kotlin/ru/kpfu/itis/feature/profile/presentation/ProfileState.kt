package ru.kpfu.itis.feature.profile.presentation

import ru.kpfu.itis.feature.profile.domain.model.Profile

data class ProfileState(
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val isEditing: Boolean = false,
    val editFullName: String = "",
    val editSkills: String = "",
    val editContactInfo: String = "",
    val editPortfolioLink: String = "",
    val editPhone: String = "",
    val isSaving: Boolean = false,
    val error: String? = null,
    val showLogoutDialog: Boolean = false
)
