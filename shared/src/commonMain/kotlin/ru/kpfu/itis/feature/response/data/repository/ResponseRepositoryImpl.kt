package ru.kpfu.itis.feature.response.data.repository

import ru.kpfu.itis.feature.response.data.remote.ResponseApi
import ru.kpfu.itis.feature.response.data.remote.model.SubmitResponseRequest
import ru.kpfu.itis.feature.response.domain.model.ResponseResult
import ru.kpfu.itis.feature.response.domain.repository.ResponseRepository

class ResponseRepositoryImpl(
    private val api: ResponseApi
) : ResponseRepository {

    override suspend fun submitResponse(
        caseId: Int,
        coverLetter: String,
        solutionLink: String
    ): Result<ResponseResult> {
        // TODO: раскомментировать когда бэкенд пришлёт контракт
        /*return try {
            val dto = api.submitResponse(caseId, SubmitResponseRequest(coverLetter, solutionLink))
            Result.success(ResponseResult(submittedAt = dto.submittedAt, status = dto.status))
        } catch (e: ClientRequestException) {
            Result.failure(Exception(when (e.response.status.value) {
                404 -> "Кейс не найден"
                409 -> "Вы уже откликались на этот кейс"
                else -> "Ошибка сервера (${e.response.status.value})"
            }))
        } catch (e: Exception) {
            Result.failure(Exception("Нет соединения"))
        }*/

        kotlinx.coroutines.delay(1200)
        return Result.success(
            ResponseResult(
                submittedAt = "13 мая 2025, 9:41",
                status = "На проверке"
            )
        )
    }
}
