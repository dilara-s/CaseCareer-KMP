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
        if (id > 10) return Result.failure(Exception("Кейс не найден"))
        return Result.success(
            CaseDetail(
                id = id,
                title = "Разработка KMP-библиотеки для работы с геолокацией",
                description = "2ГИС развивает собственную кросс-платформенную экосистему и ищет студента для разработки Kotlin Multiplatform Mobile (KMM) библиотеки, которая инкапсулирует работу с геолокацией на iOS и Android.\n\nЧто предоставляем:\n— Доступ к внутреннему SDK 2ГИС (документация + примеры)\n— Ментора из команды мобильной платформы\n— Тестовые устройства (по договорённости)\n\nЧто нужно сделать:\n1. Спроектировать общий Kotlin API для запроса разрешений на геолокацию и получения координат (expect/actual).\n2. Реализовать actual-реализации для Android (FusedLocationProviderClient) и iOS (CLLocationManager).\n3. Добавить поддержку одноразового и потокового (Flow) получения координат.\n4. Покрыть общую логику unit-тестами (≥ 80% coverage).\n5. Написать README с инструкцией по подключению через Gradle/SPM и примерами использования.\n\nТребования к стеку: Kotlin 1.9+, KMP / KMM, Coroutines + Flow, XCTest / JUnit 5.",
                reward = "55000.00",
                deadline = "2026-09-01T00:00:00Z",
                ndaRequired = true,
                status = "ACTIVE",
                companyName = "2ГИС",
                companyId = 7L,
                publishedAt = "2026-06-14T00:00:00Z",
                createdAt = "2026-06-13T00:00:00Z"
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

        val pool = listOf(
            Case(
                id = 1L,
                title = "Разработка REST API для системы управления заказами",
                reward = "45000.00",
                deadline = "2026-08-20T00:00:00Z",
                ndaRequired = false,
                status = "ACTIVE",
                companyName = "Ozon Tech",
                companyId = 1L,
                createdAt = "2026-06-10T09:00:00Z"
            ),
            Case(
                id = 2L,
                title = "Android-приложение для внутренней логистики курьеров",
                reward = "60000.00",
                deadline = "2026-09-05T00:00:00Z",
                ndaRequired = true,
                status = "ACTIVE",
                companyName = "Яндекс",
                companyId = 2L,
                createdAt = "2026-06-11T10:00:00Z"
            ),
            Case(
                id = 3L,
                title = "Разработка KMP-библиотеки для работы с геолокацией",
                reward = "55000.00",
                deadline = "2026-09-01T00:00:00Z",
                ndaRequired = false,
                status = "ACTIVE",
                companyName = "2ГИС",
                companyId = 7L,
                createdAt = "2026-06-13T11:00:00Z"
            ),
            Case(
                id = 4L,
                title = "Редизайн онбординга: от 0 до первой транзакции за 3 экрана",
                reward = "30000.00",
                deadline = "2026-07-31T00:00:00Z",
                ndaRequired = false,
                status = "ACTIVE",
                companyName = "Тинькофф",
                companyId = 3L,
                createdAt = "2026-06-09T08:00:00Z"
            ),
            Case(
                id = 5L,
                title = "Анализ воронки объявлений: почему продавцы уходят до публикации",
                reward = "40000.00",
                deadline = "2026-08-10T00:00:00Z",
                ndaRequired = true,
                status = "ACTIVE",
                companyName = "Авито",
                companyId = 4L,
                createdAt = "2026-06-12T14:00:00Z"
            ),
            Case(
                id = 6L,
                title = "Сравнительный анализ EdTech-конкурентов: стратегия выхода в B2B",
                reward = "25000.00",
                deadline = "2026-07-25T00:00:00Z",
                ndaRequired = false,
                status = "ACTIVE",
                companyName = "Яндекс Практикум",
                companyId = 8L,
                createdAt = "2026-06-08T12:00:00Z"
            ),
            Case(
                id = 7L,
                title = "Построение рекомендательной системы для музыкального стриминга",
                reward = "70000.00",
                deadline = "2026-09-15T00:00:00Z",
                ndaRequired = true,
                status = "ACTIVE",
                companyName = "VK",
                companyId = 6L,
                createdAt = "2026-06-14T09:30:00Z"
            ),
            Case(
                id = 8L,
                title = "iOS-виджет главного экрана: баланс, кешбэк и быстрые платежи",
                reward = "50000.00",
                deadline = "2026-08-28T00:00:00Z",
                ndaRequired = true,
                status = "ACTIVE",
                companyName = "Сбер",
                companyId = 5L,
                createdAt = "2026-06-13T15:00:00Z"
            ),
            Case(
                id = 9L,
                title = "Проектирование архитектуры микросервисов платёжного шлюза",
                reward = "65000.00",
                deadline = "2026-09-20T00:00:00Z",
                ndaRequired = true,
                status = "ACTIVE",
                companyName = "МТС Digital",
                companyId = 9L,
                createdAt = "2026-06-07T10:00:00Z"
            ),
            Case(
                id = 10L,
                title = "Автоматизация CI/CD для мобильных приложений на GitHub Actions",
                reward = "35000.00",
                deadline = "2026-08-01T00:00:00Z",
                ndaRequired = false,
                status = "ACTIVE",
                companyName = "Kaspersky",
                companyId = 10L,
                createdAt = "2026-06-06T11:00:00Z"
            )
        )

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