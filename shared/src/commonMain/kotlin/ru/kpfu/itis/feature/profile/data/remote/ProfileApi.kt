package ru.kpfu.itis.feature.profile.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.kpfu.itis.feature.profile.data.remote.model.ProfileDto

class ProfileApi(private val client: HttpClient) {

    suspend fun getMyProfile(): ProfileDto {
        return client.get("api/v1/profiles/me/").body()
    }
}
