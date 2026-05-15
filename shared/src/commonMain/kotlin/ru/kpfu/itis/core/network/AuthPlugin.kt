package ru.kpfu.itis.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.kpfu.itis.feature.auth.data.remote.model.RefreshRequest
import ru.kpfu.itis.feature.auth.data.remote.model.TokenResponse

private val mutex = Mutex()

fun HttpClient.installAuthPlugin(tokenStorage: TokenStorage) {
    plugin(HttpSend).intercept { request ->

        val path = request.url.encodedPath
        val isAuthPath = path.contains("auth/login") ||
                path.contains("auth/register") ||
                path.contains("token/refresh")

        if (!isAuthPath) {
            tokenStorage.accessToken?.let { token ->
                request.bearerAuth(token)
            }
        }

        var call = execute(request)

        if (call.response.status != HttpStatusCode.Unauthorized || isAuthPath) {
            return@intercept call
        }

        val newAccessToken = mutex.withLock {
            val refresh = tokenStorage.refreshToken
            if (refresh == null) {
                tokenStorage.clearTokens()
                return@withLock null
            }

            try {
                // Строим HttpRequestBuilder вручную — НЕ вызываем client.post
                val refreshRequest = HttpRequestBuilder().apply {
                    url("api/v1/auth/token/refresh/")
                    method = io.ktor.http.HttpMethod.Post
                    contentType(ContentType.Application.Json)
                    setBody(RefreshRequest(refresh))
                    headers { remove(HttpHeaders.Authorization) }
                }

                val refreshCall = execute(refreshRequest)

                if (refreshCall.response.status == HttpStatusCode.OK) {
                    val tokens = refreshCall.response.body<TokenResponse>()
                    tokenStorage.saveTokens(tokens.access, tokens.refresh)
                    tokens.access
                } else {
                    tokenStorage.clearTokens()
                    null
                }
            } catch (e: Exception) {
                tokenStorage.clearTokens()
                null
            }
        }

        if (newAccessToken != null) {
            request.headers {
                remove(HttpHeaders.Authorization)
            }
            request.bearerAuth(newAccessToken)
            call = execute(request)
        }

        call
    }
}