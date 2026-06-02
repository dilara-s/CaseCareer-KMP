package ru.kpfu.itis.feature.response.presentation

sealed interface ResponseEffect {
    data object CloseSheet : ResponseEffect

    data object NavigateToMyCases : ResponseEffect
}
