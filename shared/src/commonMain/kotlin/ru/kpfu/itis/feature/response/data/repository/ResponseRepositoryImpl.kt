package ru.kpfu.itis.feature.response.data.repository

import io.ktor.client.plugins.ClientRequestException
import ru.kpfu.itis.core.mock.MockResponseStore
import ru.kpfu.itis.feature.mycases.domain.model.MyCase
import ru.kpfu.itis.feature.response.data.remote.ResponseApi
import ru.kpfu.itis.feature.response.data.remote.model.SubmitResponseRequest
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

        // ── Реальный API ──────────────────────────────────────────────────────
        // Раскомментировать когда сервер будет задеплоен, мок-блок ниже удалить
        /*
        return try {
            val dto = api.submitResponse(
                SubmitResponseRequest(
                    caseId = caseId,
                    coverLetter = coverLetter,
                    solutionLink = solutionLink
                )
            )
            // Сохраняем в мок-стор чтобы сразу появилось в MyCases (убрать вместе с MockResponseStore)
            MockResponseStore.add(
                MyCase(
                    caseId = dto.caseId,
                    title = dto.caseTitle,
                    companyName = dto.companyName,
                    submittedAt = dto.submittedAt,
                    status = dto.status,
                    version = dto.version
                )
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
        */

        // ── Мок ───────────────────────────────────────────────────────────────
        kotlinx.coroutines.delay(1200)
        MockResponseStore.add(
            MyCase(
                caseId = caseId,
                title = caseTitle,
                companyName = companyName,
                submittedAt = "сегодня",
                status = "SENT",
                version = 1
            )
        )
        return Result.success(
            ResponseResult(
                submittedAt = "сегодня",
                status = "SENT"
            )
        )
    }
}
