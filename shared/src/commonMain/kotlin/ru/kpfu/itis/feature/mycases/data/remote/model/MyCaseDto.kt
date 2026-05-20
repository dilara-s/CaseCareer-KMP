package ru.kpfu.itis.feature.mycases.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO: поля уточнить по контракту бэкенда
@Serializable
data class MyCaseDto(
    @SerialName("case_id") val caseId: Int,
    val title: String,
    @SerialName("company_name") val companyName: String,
    @SerialName("submitted_at") val submittedAt: String,
    val status: String
)
