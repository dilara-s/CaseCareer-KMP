package ru.kpfu.itis.feature.feed.presentation

import ru.kpfu.itis.feature.feed.domain.model.Case

data class FeedState(
    val cases: List<Case> = emptyList(),
    val isLoading: Boolean = false,       // первая загрузка
    val isLoadingMore: Boolean = false,   // подгрузка следующей страницы
    val isRefreshing: Boolean = false,    // pull-to-refresh
    val error: String? = null,
    val currentPage: Int = 1,
    val hasMore: Boolean = true,
    val totalCount: Int = 0
)