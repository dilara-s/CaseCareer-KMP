package ru.kpfu.itis.feature.auth.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val access: String,
    val refresh: String,
)
