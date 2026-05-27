package ru.kpfu.itis.feature.profile.data.repository

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
        // Имя и email берём из локальной БД (сохраняются при логине/регистрации)
        val cachedUser = userDataSource.getUser()
        println("PROFILE_LOG: getMyProfile (mock), cached user = $cachedUser")

        // TODO: раскомментировать когда бэкенд будет готов
        /*return try {
            val dto = api.getMyProfile()
            Result.success(
                Profile(
                    id = cachedUser?.id?.toInt() ?: dto.id,
                    email = cachedUser?.email ?: dto.email,
                    fullName = cachedUser?.name ?: dto.fullName,
                    roleType = dto.roleType,
                    skills = dto.skills.orEmpty(),
                    contactInfo = dto.contactInfo.orEmpty(),
                    portfolioLink = dto.portfolioLink.orEmpty(),
                    phone = dto.phone.orEmpty(),
                    rating = dto.rating.orEmpty(),
                    createdAt = dto.createdAt
                )
            )
        } catch (e: ClientRequestException) {
            Result.failure(Exception(httpError(e.response.status.value)))
        } catch (e: Exception) {
            Result.failure(Exception("Нет соединения"))
        }*/

        delay(600)

        return Result.success(
            Profile(
                id = cachedUser?.id?.toInt() ?: 1,
                email = cachedUser?.email ?: "tarkv@gmail.com",
                fullName = cachedUser?.name ?: "Ksenia Taryshkina",
                roleType = "Student",
                skills = "Java, Kotlin, Git, Android, API, SQL, Figma, Jira, Scrum",
                contactInfo = "tg: @ktsaur",
                portfolioLink = "https://github.com/ktsaur",
                phone = "89172503191",
                rating = "0",
                createdAt = "2025-01-01T00:00:00Z"
            )
        )
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
