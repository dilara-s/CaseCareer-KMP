package ru.kpfu.itis.feature.profile.domain.usecase

import ru.kpfu.itis.feature.profile.domain.model.Profile
import ru.kpfu.itis.feature.profile.domain.repository.ProfileRepository

class GetProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(): Result<Profile> = repository.getMyProfile()
}
