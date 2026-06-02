package ru.kpfu.itis.feature.mycases.data.repository

import io.ktor.client.plugins.ClientRequestException
import ru.kpfu.itis.feature.mycases.data.remote.MyCasesApi
import ru.kpfu.itis.feature.mycases.data.remote.model.MyCaseDto
import ru.kpfu.itis.feature.mycases.domain.model.MyCase
import ru.kpfu.itis.feature.mycases.domain.repository.MyCasesRepository

class MyCasesRepositoryImpl(
    private val api: MyCasesApi,
) : MyCasesRepository {
    override suspend fun getMyCases(): Result<List<MyCase>> {
        return try {
            val response = api.getMyCases()
            Result.success(response.results.map { it.toDomain() })
        } catch (e: ClientRequestException) {
            Result.failure(
                Exception(
                    when (e.response.status.value) {
                        401 -> "Сессия истекла, войдите снова"
                        else -> "Ошибка сервера (${e.response.status.value})"
                    },
                ),
            )
        } catch (e: Exception) {
            Result.failure(Exception("Нет соединения"))
        }
    }
}

private fun MyCaseDto.toDomain() =
    MyCase(
        caseId = caseId,
        title = caseTitle,
        companyName = companyName,
        submittedAt = submittedAt,
        status = status,
        version = version,
    )
