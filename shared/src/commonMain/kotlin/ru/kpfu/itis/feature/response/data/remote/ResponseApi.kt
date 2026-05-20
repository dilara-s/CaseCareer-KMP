package ru.kpfu.itis.feature.response.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.kpfu.itis.feature.response.data.remote.model.SubmitResponseDto
import ru.kpfu.itis.feature.response.data.remote.model.SubmitResponseRequest

class ResponseApi(private val client: HttpClient) {

    // TODO: заменить endpoint когда бэкенд пришлёт контракт
    suspend fun submitResponse(caseId: Int, request: SubmitResponseRequest): SubmitResponseDto {
        return client.post("api/v1/cases/$caseId/respond/") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}
