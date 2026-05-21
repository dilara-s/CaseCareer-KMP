package ru.kpfu.itis.feature.profile.domain.repository

import ru.kpfu.itis.feature.profile.domain.model.Profile

interface ProfileRepository {
    suspend fun getMyProfile(): Result<Profile>
    suspend fun logout()
}
