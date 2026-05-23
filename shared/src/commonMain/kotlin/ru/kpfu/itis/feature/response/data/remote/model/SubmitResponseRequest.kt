package ru.kpfu.itis.feature.response.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Контракт согласован с бэкендом: POST /api/v1/solutions/ (JSON)
// Поля: case_id (обязательно), cover_letter (необязательно), solution_link (обязательно)
// Файл (file) убран по договорённости с командой
@Serializable
data class SubmitResponseRequest(
    @SerialName("case_id") val caseId: Int,
    @SerialName("cover_letter") val coverLetter: String,
    @SerialName("solution_link") val solutionLink: String
)
