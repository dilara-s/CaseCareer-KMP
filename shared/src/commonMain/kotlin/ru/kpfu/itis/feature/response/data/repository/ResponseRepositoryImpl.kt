package ru.kpfu.itis.feature.response.data.repository

import io.ktor.client.plugins.ClientRequestException
import ru.kpfu.itis.feature.response.data.remote.ResponseApi
import ru.kpfu.itis.feature.response.domain.model.ResponseResult
import ru.kpfu.itis.feature.response.domain.repository.ResponseRepository

class ResponseRepositoryImpl(
    private val api: ResponseApi
) : ResponseRepository {

    override suspend fun submitResponse(
        caseId: Int,
        caseTitle: String,
        companyName: String,
        coverLetter: String,
        solutionLink: String
    ): Result<ResponseResult> {
        return try {
            val dto = api.submitResponse(
                caseId = caseId,
                coverLetter = coverLetter,
                solutionLink = solutionLink
            )
            Result.success(
                ResponseResult(
                    submittedAt = dto.submittedAt,
                    status = dto.status
                )
            )
        } catch (e: ClientRequestException) {
            Result.failure(
                Exception(
                    when (e.response.status.value) {
                        400 -> "Кейс не найден или некорректные данные"
                        403 -> "Только студенты могут откликаться на кейсы"
                        422 -> "Кейс закрыт или истёк дедлайн"
                        else -> "Ошибка сервера (${e.response.status.value})"
                    }
                )
            )
        } catch (e: Exception) {
            Result.failure(Exception("Нет соединения"))
        }
    }
}
