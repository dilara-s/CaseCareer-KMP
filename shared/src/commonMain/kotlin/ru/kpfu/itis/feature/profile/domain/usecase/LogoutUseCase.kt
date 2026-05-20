package ru.kpfu.itis.feature.profile.domain.usecase

import ru.kpfu.itis.feature.profile.domain.repository.ProfileRepository

class LogoutUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke() = repository.logout()
}
