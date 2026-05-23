package ru.kpfu.itis.feature.mycases.data.remote.model

import kotlinx.serialization.Serializable

// Обёртка для paginated-ответа GET /api/v1/solutions/my/
// {"count": 3, "next": null, "previous": null, "results": [...]}
@Serializable
data class PaginatedSolutionResponseDto(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<MyCaseDto>
)
