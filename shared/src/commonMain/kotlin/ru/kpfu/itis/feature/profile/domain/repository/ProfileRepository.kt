package ru.kpfu.itis.feature.profile.domain.repository

import ru.kpfu.itis.feature.profile.domain.model.Profile

interface ProfileRepository {
    suspend fun getMyProfile(): Result<Profile>
    suspend fun updateProfile(
        fullName: String?,
        skills: String?,
        contactInfo: String?,
        portfolioLink: String?,
        phone: String?
    ): Result<Profile>
    suspend fun logout()
}
