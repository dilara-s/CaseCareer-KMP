package ru.kpfu.itis.feature.auth.domain.repository

interface AuthRepository {
    suspend fun login(
        email: String,
        password: String,
    ): Result<Unit>

    suspend fun register(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String,
        phone: String,
        contactInfo: String,
        portfolioLink: String,
        skills: String,
    ): Result<Unit>

    suspend fun logout()

    fun isLoggedIn(): Boolean
}
