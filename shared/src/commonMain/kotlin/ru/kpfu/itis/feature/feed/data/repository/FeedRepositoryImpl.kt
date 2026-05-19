package ru.kpfu.itis.feature.feed.data.repository

import kotlinx.coroutines.delay
import ru.kpfu.itis.feature.feed.data.remote.FeedApi
import ru.kpfu.itis.feature.feed.domain.model.Case
import ru.kpfu.itis.feature.feed.domain.repository.FeedRepository
import ru.kpfu.itis.feature.feed.domain.repository.FeedPage

class FeedRepositoryImpl(
    private val feedApi: FeedApi
) : FeedRepository {

    override suspend fun getCases(
        page: Int,
        q: String?,
        rewardMin: String?,
        rewardMax: String?,
        ndaRequired: Boolean?
    ): Result<FeedPage> {
        /*return try {
            val response = feedApi.getCases(page, q, rewardMin, rewardMax, ndaRequired)
            Result.success(
                CasesPage(
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
        }*/

        println("FEED_LOG: getCases called, page=$page")
        delay(1000)

        val mockCases = List(20) { i ->
            val globalIndex = (page - 1) * 20 + i + 1
            Case(
                id = globalIndex.toLong(),
                title = "Разработка ML модели #$globalIndex",
                reward = "${globalIndex * 5000}.00",
                deadline = "2026-08-15T00:00:00Z",
                ndaRequired = globalIndex % 2 == 0,
                status = "ACTIVE",
                companyName = "Sber AI Lab",
                companyId = 5L,
                createdAt = "2026-06-12T10:00:00Z"
            )
        }

        println("FEED_LOG: getCases success, page=$page, count=${mockCases.size}")
        return Result.success(
            FeedPage(
                cases = mockCases,
                totalCount = 42,
                hasNextPage = page < 3
            )
        )
    }
}