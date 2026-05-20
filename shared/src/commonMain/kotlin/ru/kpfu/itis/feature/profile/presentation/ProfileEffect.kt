package ru.kpfu.itis.feature.profile.presentation

sealed interface ProfileEffect {
    data object NavigateToAuth : ProfileEffect
}
