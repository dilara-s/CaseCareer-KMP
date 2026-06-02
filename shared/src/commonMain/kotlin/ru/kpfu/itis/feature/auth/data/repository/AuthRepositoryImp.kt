package ru.kpfu.itis.feature.auth.data.repository

import io.ktor.client.plugins.ClientRequestException
import ru.kpfu.itis.core.network.TokenStorage
import ru.kpfu.itis.feature.auth.data.datasource.UserDataSource
import ru.kpfu.itis.feature.auth.data.remote.AuthApi
import ru.kpfu.itis.feature.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val userDataSource: UserDataSource,
    private val tokenStorage: TokenStorage,
) : AuthRepository {
    override suspend fun login(
        email: String,
        password: String,
    ): Result<Unit> {
        return try {
            val response = authApi.login(email, password)
            tokenStorage.saveTokens(response.access, response.refresh)

            try {
                val profile = authApi.getMyProfile()
                userDataSource.upsertUser(profile.id, profile.email, profile.fullName)
            } catch (e: Exception) {
                println("AUTH_LOG: profile cache failed — ${e.message}")
            }

            Result.success(Unit)
        } catch (e: ClientRequestException) {
            val message =
                when (e.response.status.value) {
                    400 -> "Неверный email или пароль"
                    403 -> "Пользователь уже авторизован"
                    else -> "Ошибка: ${e.response.status.value}"
                }
            Result.failure(Exception(message))
        } catch (e: Exception) {
            Result.failure(Exception("Нет соединения"))
        }
    }

    override suspend fun register(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String,
        phone: String,
        contactInfo: String,
        portfolioLink: String,
        skills: String,
    ): Result<Unit> {
        return try {
            val response = authApi.register(fullName, email, password, confirmPassword, phone, contactInfo, portfolioLink, skills)
            tokenStorage.saveTokens(response.access, response.refresh)
            userDataSource.upsertUser(
                id = response.user.id,
                email = response.user.email,
                name = fullName,
            )
            Result.success(Unit)
        } catch (e: ClientRequestException) {
            val message =
                when (e.response.status.value) {
                    400 -> "Email уже занят или пароли не совпадают"
                    403 -> "Пользователь уже авторизован"
                    else -> "Ошибка: ${e.response.status.value}"
                }
            Result.failure(Exception(message))
        } catch (e: Exception) {
            Result.failure(Exception("Нет соединения"))
        }
    }

    override suspend fun logout() {
        tokenStorage.clearTokens()
        userDataSource.clearUser()
    }

    override fun isLoggedIn(): Boolean {
        return tokenStorage.isLoggedIn()
    }
}
