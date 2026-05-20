package ru.kpfu.itis.feature.profile.presentation

sealed class ProfileEvent {
    data object Load : ProfileEvent()
    data object StartEdit : ProfileEvent()
    data object CancelEdit : ProfileEvent()
    data class UpdateFullName(val value: String) : ProfileEvent()
    data class UpdateSkills(val value: String) : ProfileEvent()
    data class UpdateContactInfo(val value: String) : ProfileEvent()
    data class UpdatePortfolioLink(val value: String) : ProfileEvent()
    data class UpdatePhone(val value: String) : ProfileEvent()
    data object Save : ProfileEvent()
    data object ShowLogoutDialog : ProfileEvent()
    data object DismissLogoutDialog : ProfileEvent()
    data object ConfirmLogout : ProfileEvent()
}
