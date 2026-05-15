package ru.kpfu.itis.feature.auth.domain.usecase

import ru.kpfu.itis.feature.auth.domain.repository.AuthRepository
class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String,
        phone: String,
        contactInfo: String = "",
        portfolioLink: String = "",
        skills: String = ""
    ): Result<Unit> {
        if (fullName.isBlank()) return Result.failure(Exception("Введите имя"))
        if (password != confirmPassword) return Result.failure(Exception("Пароли не совпадают"))
        return repository.register(fullName, email, password, confirmPassword, phone, contactInfo, portfolioLink, skills)
    }
}