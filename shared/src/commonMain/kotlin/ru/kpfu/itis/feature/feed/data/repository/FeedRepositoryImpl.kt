package ru.kpfu.itis.feature.feed.data.repository

import kotlinx.coroutines.delay
import ru.kpfu.itis.feature.feed.data.remote.FeedApi
import ru.kpfu.itis.feature.feed.domain.model.Case
import ru.kpfu.itis.feature.feed.domain.model.CaseDetail
import ru.kpfu.itis.feature.feed.domain.repository.FeedRepository
import ru.kpfu.itis.feature.feed.domain.repository.FeedPage

class FeedRepositoryImpl(
    private val feedApi: FeedApi
) : FeedRepository {

    override suspend fun getCaseDetail(id: Long): Result<CaseDetail> {
        /*return try {
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
        }*/

        delay(800)
        if (id > 60) return Result.failure(Exception("Кейс не найден"))
        return Result.success(
            CaseDetail(
                id = id,
                title = "Анализ оттока продавцов: построение дашборда и рекомендации #$id",
                description = "Sber AI Lab столкнулась с ростом оттока продавцов в категориях электроники и товаров для дома. Нам нужен студент, который самостоятельно разберётся в данных, найдёт закономерности и предложит конкретные меры удержания.\n\nЧто предоставляем: SQL-выгрузка с анонимизированными данными за 12 месяцев — около 80 000 строк. Поля: seller_id, категория, GMV по месяцам, количество активных SKU, дни с последней продажи, тип тарифа, регион, дата регистрации, флаг оттока (0/1).\n\nЧто нужно сделать:\n1. Провести EDA — распределение оттока по категориям, регионам, возрасту аккаунта, тарифу.\n2. Определить 4–6 ключевых признаков оттока. Желательно — простая интерпретируемая модель (логрег или дерево решений), ROC-AUC ≥ 0.75.\n3. Построить интерактивный дашборд в Tableau Public или Apache Superset с фильтрами по категории, региону, периоду.\n4. Подготовить список из 3–5 рекомендаций по удержанию с оценкой потенциального эффекта.",
                reward = "${id * 5000}.00",
                deadline = "2026-08-15T00:00:00Z",
                ndaRequired = id % 2 == 0L,
                status = "ACTIVE",
                companyName = "Sber AI Lab",
                companyId = 5L,
                publishedAt = "2026-06-12T00:00:00Z",
                createdAt = "2026-06-10T00:00:00Z"
            )
        )
    }

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