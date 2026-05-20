package ru.kpfu.itis.feature.profile.domain.usecase

import ru.kpfu.itis.feature.profile.domain.model.Profile
import ru.kpfu.itis.feature.profile.domain.repository.ProfileRepository

class UpdateProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(
        fullName: String?,
        skills: String?,
        contactInfo: String?,
        portfolioLink: String?,
        phone: String?
    ): Result<Profile> = repository.updateProfile(fullName, skills, contactInfo, portfolioLink, phone)
}
