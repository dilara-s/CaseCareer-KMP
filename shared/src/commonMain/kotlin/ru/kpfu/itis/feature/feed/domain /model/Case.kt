package ru.kpfu.itis.feature.feed.domain.model

import ru.kpfu.itis.feature.feed.data.remote.model.CaseResponse

data class Case(
    val id: Long,
    val title: String,
    val reward: String,
    val deadline: String,
    val ndaRequired: Boolean,
    val status: String,
    val companyName: String,
    val companyId: Long,
    val createdAt: String,
)

fun CaseResponse.toDomain() =
    Case(
        id = id,
        title = title,
        reward = reward,
        deadline = deadline,
        ndaRequired = ndaRequired,
        status = status,
        companyName = companyName,
        companyId = companyId,
        createdAt = createdAt,
    )
