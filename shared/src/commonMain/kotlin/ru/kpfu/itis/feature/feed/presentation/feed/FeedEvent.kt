package ru.kpfu.itis.feature.feed.presentation.feed

sealed class FeedEvent {
    object LoadNextPage : FeedEvent()

    object Refresh : FeedEvent()

    data class CaseClicked(val caseId: Long) : FeedEvent()

    data class SearchQueryChanged(val query: String) : FeedEvent()

    object ClearSearch : FeedEvent()
}
