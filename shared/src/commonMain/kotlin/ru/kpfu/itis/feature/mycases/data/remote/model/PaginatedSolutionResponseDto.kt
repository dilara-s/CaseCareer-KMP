package ru.kpfu.itis.feature.mycases.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedSolutionResponseDto(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<MyCaseDto>,
)
