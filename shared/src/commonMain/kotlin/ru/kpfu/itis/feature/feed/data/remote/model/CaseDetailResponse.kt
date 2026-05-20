package ru.kpfu.itis.feature.feed.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CaseDetailResponse(
    val id: Long,
    val title: String,
    val description: String,
    val reward: String,
    val deadline: String,
    @SerialName("nda_required") val ndaRequired: Boolean,
    val status: String,
    @SerialName("company_name") val companyName: String,
    @SerialName("company_id") val companyId: Long,
    @SerialName("published_at") val publishedAt: String,
    @SerialName("created_at") val createdAt: String
)