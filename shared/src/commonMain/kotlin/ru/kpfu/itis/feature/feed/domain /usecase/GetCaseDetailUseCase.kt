package ru.kpfu.itis.feature.feed.domain.usecase

import ru.kpfu.itis.feature.feed.domain.model.CaseDetail
import ru.kpfu.itis.feature.feed.domain.repository.FeedRepository

class GetCaseDetailUseCase(private val repository: FeedRepository) {
    suspend operator fun invoke(id: Long): Result<CaseDetail> = repository.getCaseDetail(id)
}
