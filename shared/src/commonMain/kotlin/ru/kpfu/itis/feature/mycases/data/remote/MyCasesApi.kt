package ru.kpfu.itis.feature.mycases.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.kpfu.itis.feature.mycases.data.remote.model.MyCaseDto

class MyCasesApi(private val client: HttpClient) {

    // TODO: заменить endpoint когда бэкенд пришлёт контракт
    suspend fun getMyCases(): List<MyCaseDto> {
        return client.get("api/v1/responses/").body()
    }
}
