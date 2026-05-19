package ru.kpfu.itis.feature.feed.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import ru.kpfu.itis.feature.feed.data.remote.model.CasesPagedResponse

class FeedApi(private val client: HttpClient) {

    suspend fun getCases(
        page: Int,
        q: String? = null,
        rewardMin: String? = null,
        rewardMax: String? = null,
        ndaRequired: Boolean? = null
    ): CasesPagedResponse {
        return client.get("api/v1/cases/") {
            parameter("page", page)
            q?.let { parameter("q", it) }
            rewardMin?.let { parameter("reward_min", it) }
            rewardMax?.let { parameter("reward_max", it) }
            ndaRequired?.let { parameter("nda_required", it) }
        }.body()
    }
}