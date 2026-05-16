package ru.kpfu.itis.feature.auth.presentation

sealed interface AuthEffect {
    data object NavigateToMain : AuthEffect
}