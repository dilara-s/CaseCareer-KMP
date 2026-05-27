package ru.kpfu.itis.feature.profile.data.repository

import io.ktor.client.plugins.ClientRequestException
import ru.kpfu.itis.core.network.TokenStorage
import ru.kpfu.itis.feature.auth.data.datasource.UserDataSource
import ru.kpfu.itis.feature.profile.data.remote.ProfileApi
import ru.kpfu.itis.feature.profile.data.remote.model.ProfileDto
import ru.kpfu.itis.feature.profile.domain.model.Profile
import ru.kpfu.itis.feature.profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val api: ProfileApi,
    private val tokenStorage: TokenStorage,
    private val userDataSource: UserDataSource
) : ProfileRepository {

    override suspend fun getMyProfile(): Result<Profile> {
        return try {
            val dto = api.getMyProfile()
            Result.success(dto.toDomain())
        } catch (e: ClientRequestException) {
            Result.failure(Exception(httpError(e.response.status.value)))
        } catch (e: Exception) {
            Result.failure(Exception("Нет соединения"))
        }
    }

    override suspend fun logout() {
        tokenStorage.clearTokens()
        userDataSource.clearUser()
    }

    private fun httpError(code: Int) = when (code) {
        400 -> "Некорректные данные"
        401 -> "Сессия истекла, войдите снова"
        else -> "Ошибка сервера ($code)"
    }
}

private fun ProfileDto.toDomain() = Profile(
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
