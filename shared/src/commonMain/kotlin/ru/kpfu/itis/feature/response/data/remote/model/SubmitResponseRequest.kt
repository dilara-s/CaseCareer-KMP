package ru.kpfu.itis.feature.response.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubmitResponseRequest(
    @SerialName("case_id") val caseId: Int,
    @SerialName("cover_letter") val coverLetter: String = "",
    @SerialName("solution_link") val solutionLink: String,
)
