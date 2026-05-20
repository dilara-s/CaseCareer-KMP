package ru.kpfu.itis.feature.response.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO: поля уточнить по контракту бэкенда
@Serializable
data class SubmitResponseRequest(
    @SerialName("cover_letter") val coverLetter: String,
    @SerialName("solution_link") val solutionLink: String
)
