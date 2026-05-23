package ru.kpfu.itis.feature.mycases.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.kpfu.itis.feature.mycases.data.remote.model.MyCaseDto
import ru.kpfu.itis.feature.mycases.data.remote.model.PaginatedSolutionResponseDto

class MyCasesApi(private val client: HttpClient) {

    // GET /api/v1/solutions/my/ — список откликов текущего студента (paginated, page_size=20)
    // Раскомментировать когда сервер будет задеплоен
    /*
    suspend fun getMyCases(): PaginatedSolutionResponseDto {
        return client.get("api/v1/solutions/my/").body()
    }
    */
}
