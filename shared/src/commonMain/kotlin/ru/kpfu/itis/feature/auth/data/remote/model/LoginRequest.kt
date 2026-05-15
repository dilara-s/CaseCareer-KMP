package ru.kpfu.itis.feature.auth.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)
