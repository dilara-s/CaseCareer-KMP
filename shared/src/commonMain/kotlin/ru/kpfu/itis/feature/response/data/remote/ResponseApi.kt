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

    // POST /api/v1/solutions/ — отправка отклика
    // Контракт: JSON с полями case_id, cover_letter, solution_link
    // (файл убран по договорённости с командой бэкенда)
    // Раскомментировать когда сервер будет задеплоен
    /*
    suspend fun submitResponse(request: SubmitResponseRequest): SubmitResponseDto {
        return client.post("api/v1/solutions/") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
    */
}
