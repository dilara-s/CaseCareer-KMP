package ru.kpfu.itis.feature.feed.presentation.feed

sealed interface FeedEffect {
    data class NavigateToCaseDetail(val caseId: Long) : FeedEffect
    data class ShowError(val message: String) : FeedEffect
}