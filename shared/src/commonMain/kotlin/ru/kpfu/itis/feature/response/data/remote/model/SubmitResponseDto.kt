package ru.kpfu.itis.feature.response.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO: поля уточнить по контракту бэкенда
@Serializable
data class SubmitResponseDto(
    @SerialName("submitted_at") val submittedAt: String,
    val status: String
)
