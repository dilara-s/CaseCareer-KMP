package ru.kpfu.itis.feature.feed.domain.usecase

import ru.kpfu.itis.feature.feed.domain.repository.FeedRepository
import ru.kpfu.itis.feature.feed.domain.repository.FeedPage

class GetCasesUseCase(private val repository: FeedRepository) {
    suspend operator fun invoke(
        page: Int,
        q: String? = null,
        rewardMin: String? = null,
        rewardMax: String? = null,
        ndaRequired: Boolean? = null
    ): Result<FeedPage> = repository.getCases(page, q, rewardMin, rewardMax, ndaRequired)
}
