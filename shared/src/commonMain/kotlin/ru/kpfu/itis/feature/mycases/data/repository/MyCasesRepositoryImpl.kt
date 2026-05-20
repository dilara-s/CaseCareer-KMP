package ru.kpfu.itis.feature.mycases.data.repository

import ru.kpfu.itis.core.mock.MockResponseStore
import ru.kpfu.itis.feature.mycases.data.remote.MyCasesApi
import ru.kpfu.itis.feature.mycases.domain.model.MyCase
import ru.kpfu.itis.feature.mycases.domain.repository.MyCasesRepository

class MyCasesRepositoryImpl(
    private val api: MyCasesApi
) : MyCasesRepository {

    override suspend fun getMyCases(): Result<List<MyCase>> {
        // TODO: раскомментировать когда бэкенд пришлёт контракт
        /*return try {
            val dtos = api.getMyCases()
            Result.success(dtos.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(Exception("Нет соединения"))
        }*/

        kotlinx.coroutines.delay(800)
        return Result.success(MockResponseStore.getAll())
    }
}
