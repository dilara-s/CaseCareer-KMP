package ru.kpfu.itis.feature.auth.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val user: UserDto,
    val access: String,
    val refresh: String
)
@Serializable
data class UserDto(
    val id: Long,
    val email: String
)