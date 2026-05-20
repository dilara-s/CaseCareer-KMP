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

        println("FEED_LOG: getCases called, page=$page, q=$q")
        delay(1000)

        val pool = List(60) { i ->
            val idx = i + 1
            Case(
                id = idx.toLong(),
                title = "Разработка ML модели #$idx",
                reward = "${idx * 5000}.00",
                deadline = "2026-08-15T00:00:00Z",
                ndaRequired = idx % 2 == 0,
                status = "ACTIVE",
                companyName = "Sber AI Lab",
                companyId = 5L,
                createdAt = "2026-06-12T10:00:00Z"
            )
        }

        val filtered = if (!q.isNullOrBlank()) {
            pool.filter { it.title.contains(q, ignoreCase = true) }
        } else pool

        val totalFiltered = filtered.size
        val start = (page - 1) * 20
        val pageItems = filtered.drop(start).take(20)

        println("FEED_LOG: getCases success, page=$page, total=$totalFiltered, returned=${pageItems.size}")
        return Result.success(
            FeedPage(
                cases = pageItems,
                totalCount = totalFiltered,
                hasNextPage = start + 20 < totalFiltered
            )
        )
    }
}