package ru.kpfu.itis.feature.auth.presentation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kpfu.itis.core.viewmodel.CommonViewModel
import ru.kpfu.itis.feature.auth.domain.usecase.LoginUseCase
import ru.kpfu.itis.feature.auth.domain.usecase.RegisterUseCase

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
) : CommonViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AuthEffect>(extraBufferCapacity = 1)
    val effect: SharedFlow<AuthEffect> = _effect.asSharedFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.UpdateEmail -> _state.update { it.copy(email = event.email, emailError = null, error = null) }
            is AuthEvent.UpdatePassword -> _state.update { it.copy(password = event.password, passwordError = null, error = null) }
            is AuthEvent.TogglePasswordVisibility -> _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            is AuthEvent.LoginSubmit -> login()
            is AuthEvent.GoToRegister -> _state.update { AuthState(screenMode = AuthScreenMode.RegisterStep1) }

            is AuthEvent.UpdateFullName -> _state.update { it.copy(fullName = event.name, fullNameError = null) }
            is AuthEvent.UpdatePhone -> _state.update { it.copy(phone = event.phone, phoneError = null) }
            is AuthEvent.UpdateConfirmPassword -> _state.update { it.copy(confirmPassword = event.confirm, confirmPasswordError = null) }
            is AuthEvent.RegisterStep1Next -> validateAndGoToStep2()
            is AuthEvent.GoToLogin -> _state.update { AuthState(screenMode = AuthScreenMode.Login) }

            is AuthEvent.UpdateContactInfo -> _state.update { it.copy(contactInfo = event.info) }
            is AuthEvent.UpdatePortfolioLink -> _state.update { it.copy(portfolioLink = event.link) }
            is AuthEvent.UpdateSkills -> _state.update { it.copy(skills = event.skills) }
            is AuthEvent.RegisterSubmit -> register()
            is AuthEvent.BackToStep1 -> _state.update { it.copy(screenMode = AuthScreenMode.RegisterStep1, error = null) }
        }
    }

    private fun validateAndGoToStep2() {
        val s = _state.value
        val nameError = if (s.fullName.isBlank()) "Введите имя и фамилию" else null
        val phoneError = if (s.phone.isBlank()) "Введите номер телефона" else null
        val emailError = if (!isValidEmail(s.email)) "Некорректный email" else null
        val passError = if (s.password.length < 8) "Минимум 8 символов" else null
        val confirmError = if (s.password != s.confirmPassword) "Пароли не совпадают" else null

        _state.update {
            it.copy(
                fullNameError = nameError,
                phoneError = phoneError,
                emailError = emailError,
                passwordError = passError,
                confirmPasswordError = confirmError
            )
        }

        if (listOf(nameError, phoneError, emailError, passError, confirmError).all { it == null }) {
            _state.update { it.copy(screenMode = AuthScreenMode.RegisterStep2) }
        }
    }

    private fun login() {
        val s = _state.value
        val emailError = if (!isValidEmail(s.email)) "Некорректный email" else null
        val passError = if (s.password.isBlank()) "Введите пароль" else null

        if (emailError != null || passError != null) {
            _state.update { it.copy(emailError = emailError, passwordError = passError) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            loginUseCase(s.email, s.password)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(AuthEffect.NavigateToMain)
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    private fun register() {
        val s = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            registerUseCase(
                fullName = s.fullName,
                email = s.email,
                password = s.password,
                confirmPassword = s.confirmPassword,
                phone = s.phone,
                contactInfo = s.contactInfo,
                portfolioLink = s.portfolioLink,
                skills = s.skills
            )
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(AuthEffect.NavigateToMain)
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    private fun isValidEmail(email: String) =
        Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(email)
}