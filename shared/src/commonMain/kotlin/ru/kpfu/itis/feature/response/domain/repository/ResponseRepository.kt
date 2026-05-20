package ru.kpfu.itis.feature.response.domain.repository

import ru.kpfu.itis.feature.response.domain.model.ResponseResult

interface ResponseRepository {
    suspend fun submitResponse(
        caseId: Int,
        coverLetter: String,
        solutionLink: String
    ): Result<ResponseResult>
}
