package ru.kpfu.itis.feature.profile.data.repository

import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.delay
import ru.kpfu.itis.core.network.TokenStorage
import ru.kpfu.itis.feature.auth.data.datasource.UserDataSource
import ru.kpfu.itis.feature.profile.data.remote.ProfileApi
import ru.kpfu.itis.feature.profile.domain.model.Profile
import ru.kpfu.itis.feature.profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val api: ProfileApi,
    private val tokenStorage: TokenStorage,
    private val userDataSource: UserDataSource
) : ProfileRepository {

    override suspend fun getMyProfile(): Result<Profile> {
        // TODO: раскомментировать когда бэкенд пришлёт рабочий URL
        /*return try {
            val dto = api.getMyProfile()
            Result.success(dto.toDomain())
        } catch (e: ClientRequestException) {
            Result.failure(Exception(httpError(e.response.status.value)))
        } catch (e: Exception) {
            Result.failure(Exception("Нет соединения"))
        }*/

        delay(600)

        // Берём имя/email из локального кэша (сохраняется при логине/регистрации)
        val cachedUser = userDataSource.getUser()
        println("PROFILE_LOG: getMyProfile (mock), cached user = $cachedUser")

        return Result.success(
            Profile(
                id = cachedUser?.id?.toInt() ?: 1,
                email = cachedUser?.email ?: "test@test.com",
                fullName = cachedUser?.name ?: "Тестовый Пользователь",
                roleType = "STUDENT",
                skills = tokenStorage.profileSkills ?: "",
                contactInfo = tokenStorage.profileContactInfo ?: "",
                portfolioLink = tokenStorage.profilePortfolioLink ?: "",
                phone = tokenStorage.profilePhone ?: "",
                rating = "4.75",
                createdAt = "2025-01-01T00:00:00Z"
            )
        )
    }

    override suspend fun logout() {
        tokenStorage.clearTokens()
        tokenStorage.clearProfile()
        userDataSource.clearUser()
    }

    private fun httpError(code: Int) = when (code) {
        400 -> "Некорректные данные"
        401 -> "Сессия истекла, войдите снова"
        else -> "Ошибка сервера ($code)"
    }
}

private fun ru.kpfu.itis.feature.profile.data.remote.model.ProfileDto.toDomain() = Profile(
    id = id,
    email = email,
    fullName = fullName,
    roleType = roleType,
    skills = skills.orEmpty(),
    contactInfo = contactInfo.orEmpty(),
    portfolioLink = portfolioLink.orEmpty(),
    phone = phone.orEmpty(),
    rating = rating.orEmpty(),
    createdAt = createdAt
)
