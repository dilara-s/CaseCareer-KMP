package ru.kpfu.itis.feature.mycases.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import ru.kpfu.itis.feature.mycases.data.remote.model.PaginatedSolutionResponseDto

class MyCasesApi(private val client: HttpClient) {

    suspend fun getMyCases(page: Int = 1): PaginatedSolutionResponseDto {
        return client.get("api/v1/solutions/my/") {
            if (page > 1) parameter("page", page)
        }.body()
    }
}
