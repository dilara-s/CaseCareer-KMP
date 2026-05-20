package ru.kpfu.itis.feature.profile.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.kpfu.itis.feature.profile.data.remote.model.ProfileDto
import ru.kpfu.itis.feature.profile.data.remote.model.UpdateProfileRequest

class ProfileApi(private val client: HttpClient) {

    suspend fun getMyProfile(): ProfileDto {
        return client.get("api/v1/profiles/me/").body()
    }

    suspend fun updateProfile(request: UpdateProfileRequest): ProfileDto {
        return client.patch("api/v1/profiles/me/") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}
