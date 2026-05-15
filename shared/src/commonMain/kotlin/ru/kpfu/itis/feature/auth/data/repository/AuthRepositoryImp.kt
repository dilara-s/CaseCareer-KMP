package ru.kpfu.itis.feature.auth.data.repository

import io.ktor.client.plugins.ClientRequestException
import ru.kpfu.itis.core.network.TokenStorage
import ru.kpfu.itis.feature.auth.data.datasource.UserDataSource
import ru.kpfu.itis.feature.auth.data.remote.AuthApi
import ru.kpfu.itis.feature.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val userDataSource: UserDataSource,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> {
        /*return try {
            val response = authApi.login(email, password)
            tokenStorage.saveTokens(response.access, response.refresh)
            Result.success(Unit)
        } catch (e: ClientRequestException) {
            val message = when (e.response.status.value) {
                400 -> "Неверный email или пароль"
                403 -> "Пользователь уже авторизован"
                else -> "Ошибка: ${e.response.status.value}"
            }
            Result.failure(Exception(message))
        } catch (e: Exception) {
            Result.failure(Exception("Нет соединения"))
        }*/
        kotlinx.coroutines.delay(1000)

        return if (email == "test@test.com" && password == "password123") {
            println("AUTH_LOG: login success for $email")
            tokenStorage.saveTokens(
                access = "mock_access_token",
                refresh = "mock_refresh_token"
            )
            Result.success(Unit)
        } else {
            println("AUTH_LOG: login failed — wrong credentials")
            Result.failure(Exception("Неверный email или пароль"))
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
        skills: String
    ):Result<Unit> {

        kotlinx.coroutines.delay(1000)

        println("AUTH_LOG: register called — name=$fullName, email=$email, phone=$phone")

        tokenStorage.saveTokens(
            access = "mock_access_token",
            refresh = "mock_refresh_token"
        )
        userDataSource.upsertUser(
            id = 1L,
            email = email,
            name = fullName
        )

        println("AUTH_LOG: register success, tokens saved")
        return Result.success(Unit)
        /*return try {
            val response = authApi.register(fullName, email, password, confirmPassword)
            tokenStorage.saveTokens(response.access, response.refresh)
            // Кэшируем юзера локально
            userDataSource.upsertUser(
                id = response.user.id,
                email = response.user.email,
                name = fullName
            )
            Result.success(Unit)
        } catch (e: ClientRequestException) {
            val message = when (e.response.status.value) {
                400 -> "Email уже занят или пароли не совпадают"
                403 -> "Пользователь уже авторизован"
                else -> "Ошибка: ${e.response.status.value}"
            }
            Result.failure(Exception(message))
        } catch (e: Exception) {
            Result.failure(Exception("Нет соединения"))
        }*/
    }

    override suspend fun logout() {
        println("AUTH_LOG: logout")
        tokenStorage.clearTokens()
        userDataSource.clearUser()
    }

    override fun isLoggedIn(): Boolean {
        val result = tokenStorage.isLoggedIn()
        println("AUTH_LOG: isLoggedIn = $result")
        return result
    }
}