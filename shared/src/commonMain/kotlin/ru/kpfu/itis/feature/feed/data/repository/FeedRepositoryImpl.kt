package ru.kpfu.itis.feature.feed.data.repository

import io.ktor.client.plugins.ClientRequestException
import ru.kpfu.itis.feature.feed.data.remote.FeedApi
import ru.kpfu.itis.feature.feed.domain.model.CaseDetail
import ru.kpfu.itis.feature.feed.domain.model.toDomain
import ru.kpfu.itis.feature.feed.domain.repository.FeedRepository
import ru.kpfu.itis.feature.feed.domain.repository.FeedPage

class FeedRepositoryImpl(
    private val feedApi: FeedApi
) : FeedRepository {

    override suspend fun getCaseDetail(id: Long): Result<CaseDetail> {
        return try {
            val response = feedApi.getCaseDetail(id)
            Result.success(response.toDomain())
        } catch (e: ClientRequestException) {
            val message = when (e.response.status.value) {
                401 -> "Необходима авторизация"
                404 -> "Кейс не найден"
                else -> "Ошибка: ${e.response.status.value}"
            }
            Result.failure(Exception(message))
        } catch (e: Exception) {
            Result.failure(Exception("Нет соединения"))
        }
    }

    override suspend fun getCases(
        page: Int,
        q: String?,
        rewardMin: String?,
        rewardMax: String?,
        ndaRequired: Boolean?
    ): Result<FeedPage> {
        return try {
            val response = feedApi.getCases(page, q, rewardMin, rewardMax, ndaRequired)
            Result.success(
                FeedPage(
                    cases = response.results.map { it.toDomain() },
                    totalCount = response.count,
                    hasNextPage = response.next != null
                )
            )
        } catch (e: ClientRequestException) {
            val message = when (e.response.status.value) {
                401 -> "Необходима авторизация"
                else -> "Ошибка: ${e.response.status.value}"
            }
            Result.failure(Exception(message))
        } catch (e: Exception) {
            Result.failure(Exception("Нет соединения"))
        }
    }
}
