package ru.kpfu.itis.feature.auth.domain.usecase

import ru.kpfu.itis.feature.auth.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        if (!isValidEmail(email)) return Result.failure(Exception("Некорректный email"))
        if (password.length < 8) return Result.failure(Exception("Пароль минимум 8 символов"))
        return repository.login(email, password)
    }
    private fun isValidEmail(email: String) =
        Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(email)
}