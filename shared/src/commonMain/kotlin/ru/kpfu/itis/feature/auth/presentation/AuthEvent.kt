package ru.kpfu.itis.feature.auth.presentation

sealed class AuthEvent {
    data class UpdateEmail(val email: String) : AuthEvent()
    data class UpdatePassword(val password: String) : AuthEvent()
    data object TogglePasswordVisibility : AuthEvent()
    data object LoginSubmit : AuthEvent()
    data object GoToRegister : AuthEvent()

    data class UpdateFullName(val name: String) : AuthEvent()
    data class UpdatePhone(val phone: String) : AuthEvent()
    data class UpdateConfirmPassword(val confirm: String) : AuthEvent()
    data object RegisterStep1Next : AuthEvent()
    data object GoToLogin : AuthEvent()

    data class UpdateContactInfo(val info: String) : AuthEvent()
    data class UpdatePortfolioLink(val link: String) : AuthEvent()
    data class UpdateSkills(val skills: String) : AuthEvent()
    data object RegisterSubmit : AuthEvent()
    data object BackToStep1 : AuthEvent()
}