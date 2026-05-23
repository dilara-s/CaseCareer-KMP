package ru.kpfu.itis.feature.response.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Соответствует схеме SolutionResponse из swagger
@Serializable
data class SubmitResponseDto(
    val id: Int,
    @SerialName("case_id") val caseId: Int,
    @SerialName("case_title") val caseTitle: String,
    @SerialName("company_name") val companyName: String,
    val status: String,
    val version: Int,
    @SerialName("submitted_at") val submittedAt: String
)
