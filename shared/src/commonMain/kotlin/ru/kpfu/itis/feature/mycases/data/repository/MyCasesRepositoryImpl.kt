package ru.kpfu.itis.feature.mycases.data.repository

import io.ktor.client.plugins.ClientRequestException
import ru.kpfu.itis.core.mock.MockResponseStore
import ru.kpfu.itis.feature.mycases.data.remote.MyCasesApi
import ru.kpfu.itis.feature.mycases.domain.model.MyCase
import ru.kpfu.itis.feature.mycases.domain.repository.MyCasesRepository

class MyCasesRepositoryImpl(
    private val api: MyCasesApi
) : MyCasesRepository {

    override suspend fun getMyCases(): Result<List<MyCase>> {

        // ── Реальный API ──────────────────────────────────────────────────────
        // Раскомментировать когда сервер будет задеплоен, мок-блок ниже удалить
        /*
        return try {
            val response = api.getMyCases()
            Result.success(response.results.map { it.toDomain() })
        } catch (e: ClientRequestException) {
            Result.failure(
                Exception(
                    when (e.response.status.value) {
                        401 -> "Сессия истекла, войдите снова"
                        else -> "Ошибка сервера (${e.response.status.value})"
                    }
                )
            )
        } catch (e: Exception) {
            Result.failure(Exception("Нет соединения"))
        }
        */

        // ── Мок ───────────────────────────────────────────────────────────────
        kotlinx.coroutines.delay(800)
        return Result.success(MockResponseStore.getAll())
    }
}

// Маппинг DTO → доменная модель (используется в реальном блоке выше)
private fun ru.kpfu.itis.feature.mycases.data.remote.model.MyCaseDto.toDomain() = MyCase(
    caseId = caseId,
    title = caseTitle,
    companyName = companyName,
    submittedAt = submittedAt,
    status = status,
    version = version
)
