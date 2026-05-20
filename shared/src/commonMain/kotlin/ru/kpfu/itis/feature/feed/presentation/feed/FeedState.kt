package ru.kpfu.itis.feature.feed.presentation.feed

import ru.kpfu.itis.feature.feed.domain.model.Case

data class FeedState(
    val cases: List<Case> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val hasMore: Boolean = true,
    val totalCount: Int = 0,
    val searchQuery: String = ""
)