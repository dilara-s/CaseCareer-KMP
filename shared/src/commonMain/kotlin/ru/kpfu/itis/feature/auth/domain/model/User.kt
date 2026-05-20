package ru.kpfu.itis.feature.auth.domain.model

data class User(
    val id: Long,
    val email: String,
    val name: String
)

