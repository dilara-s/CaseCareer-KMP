package ru.kpfu.itis.feature.feed.domain.model

import ru.kpfu.itis.feature.feed.data.remote.model.CaseDetailResponse

data class CaseDetail(
    val id: Long,
    val title: String,
    val description: String,
    val reward: String,
    val deadline: String,
    val ndaRequired: Boolean,
    val status: String,
    val companyName: String,
    val companyId: Long,
    val publishedAt: String,
    val createdAt: String
)

fun CaseDetailResponse.toDomain() = CaseDetail(
    id = id,
    title = title,
    description = description,
    reward = reward,
    deadline = deadline,
    ndaRequired = ndaRequired,
    status = status,
    companyName = companyName,
    companyId = companyId,
    publishedAt = publishedAt,
    createdAt = createdAt
)
