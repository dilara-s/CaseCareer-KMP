package ru.kpfu.itis.feature.response.domain.usecase

import ru.kpfu.itis.feature.response.domain.model.ResponseResult
import ru.kpfu.itis.feature.response.domain.repository.ResponseRepository

class SubmitResponseUseCase(private val repository: ResponseRepository) {
    suspend operator fun invoke(
        caseId: Int,
        coverLetter: String,
        solutionLink: String
    ): Result<ResponseResult> {
        if (coverLetter.isBlank()) return Result.failure(Exception("Напишите сопроводительное письмо"))
        return repository.submitResponse(caseId, coverLetter, solutionLink)
    }
}
