package ru.kpfu.itis.feature.feed.domain.repository

import ru.kpfu.itis.feature.feed.domain.model.Case
import ru.kpfu.itis.feature.feed.domain.model.CaseDetail

interface FeedRepository {
    suspend fun getCaseDetail(id: Long): Result<CaseDetail>

    suspend fun getCases(
        page: Int,
        q: String? = null,
        rewardMin: String? = null,
        rewardMax: String? = null,
        ndaRequired: Boolean? = null,
    ): Result<FeedPage>
}

data class FeedPage(
    val cases: List<Case>,
    val totalCount: Int,
    val hasNextPage: Boolean,
)
