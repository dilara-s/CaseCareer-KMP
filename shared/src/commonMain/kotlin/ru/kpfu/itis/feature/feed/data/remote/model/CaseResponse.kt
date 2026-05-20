package ru.kpfu.itis.feature.feed.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CaseResponse(
    val id: Long,
    val title: String,
    val reward: String,
    val deadline: String,
    @SerialName("nda_required") val ndaRequired: Boolean,
    val status: String,
    @SerialName("company_name") val companyName: String,
    @SerialName("company_id") val companyId: Long,
    @SerialName("created_at") val createdAt: String,
)

@Serializable
data class CasesPagedResponse(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<CaseResponse>
)