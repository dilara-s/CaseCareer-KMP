package ru.kpfu.itis.feature.auth.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.kpfu.itis.feature.auth.data.remote.model.LoginRequest
import ru.kpfu.itis.feature.auth.data.remote.model.ProfileResponse
import ru.kpfu.itis.feature.auth.data.remote.model.RefreshRequest
import ru.kpfu.itis.feature.auth.data.remote.model.RegisterRequest
import ru.kpfu.itis.feature.auth.data.remote.model.RegisterResponse
import ru.kpfu.itis.feature.auth.data.remote.model.TokenResponse

class AuthApi(private val client: HttpClient) {

    suspend fun login(email: String, password: String): TokenResponse {
        return client.post("api/v1/auth/login/") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }.body()
    }

    suspend fun register(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): RegisterResponse {
        return client.post("api/v1/auth/register/user/") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(fullName, email, password, confirmPassword))
        }.body()
    }

    suspend fun refreshToken(refresh: String): TokenResponse {
        return client.post("api/v1/auth/token/refresh/") {
            contentType(ContentType.Application.Json)
            setBody(RefreshRequest(refresh))
        }.body()
    }

    suspend fun getMyProfile(): ProfileResponse {
        return client.get("api/v1/profiles/me/").body()
    }

}