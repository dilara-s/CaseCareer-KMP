package ru.kpfu.itis.feature.feed.domain.repository

import ru.kpfu.itis.feature.feed.domain.model.Case


interface FeedRepository {
    suspend fun getCases(
        page: Int,
        q: String? = null,
        rewardMin: String? = null,
        rewardMax: String? = null,
        ndaRequired: Boolean? = null
    ): Result<FeedPage>
}

data class FeedPage(
    val cases: List<Case>,
    val totalCount: Int,
    val hasNextPage: Boolean
)