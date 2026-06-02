package ru.kpfu.itis.feature.profile.presentation

sealed class ProfileEvent {
    data object Load : ProfileEvent()

    data object ShowLogoutDialog : ProfileEvent()

    data object DismissLogoutDialog : ProfileEvent()

    data object ConfirmLogout : ProfileEvent()

    data object ShowDeleteAccountDialog : ProfileEvent()

    data object DismissDeleteAccountDialog : ProfileEvent()

    data object ConfirmDeleteAccount : ProfileEvent()
}
