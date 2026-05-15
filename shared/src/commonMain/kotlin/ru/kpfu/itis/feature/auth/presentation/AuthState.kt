package ru.kpfu.itis.feature.auth.presentation

data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val screenMode: AuthScreenMode = AuthScreenMode.Login,

    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,

    val fullName: String = "",
    val phone: String = "",
    val confirmPassword: String = "",
    val fullNameError: String? = null,
    val phoneError: String? = null,
    val confirmPasswordError: String? = null,

    val contactInfo: String = "",
    val portfolioLink: String = "",
    val skills: String = "",
)

enum class AuthScreenMode {
    Login,
    RegisterStep1,
    RegisterStep2
}