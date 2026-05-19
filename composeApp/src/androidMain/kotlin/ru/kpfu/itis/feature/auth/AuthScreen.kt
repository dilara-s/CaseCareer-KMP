package ru.kpfu.itis.feature.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import ru.kpfu.itis.designSystem.component.AuthButton
import ru.kpfu.itis.designSystem.component.AuthTextField
import ru.kpfu.itis.designSystem.component.ClickableText
import ru.kpfu.itis.feature.auth.presentation.AuthEffect
import ru.kpfu.itis.feature.auth.presentation.AuthEvent
import ru.kpfu.itis.feature.auth.presentation.AuthScreenMode
import ru.kpfu.itis.feature.auth.presentation.AuthState
import ru.kpfu.itis.feature.auth.presentation.AuthViewModel


@Composable
fun AuthRoute(
    navController: NavHostController,
    viewModel: AuthViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                AuthEffect.NavigateToMain -> navController.navigate("main") {
                    popUpTo("auth") { inclusive = true }
                }
            }
        }
    }

    when (state.screenMode) {
        AuthScreenMode.Login -> LoginScreen(state, viewModel::onEvent)
        AuthScreenMode.RegisterStep1 -> RegisterStep1Screen(state, viewModel::onEvent)
        AuthScreenMode.RegisterStep2 -> RegisterStep2Screen(state, viewModel::onEvent)
    }
}


@Composable
private fun LoginScreen(state: AuthState, onEvent: (AuthEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(80.dp))

        Text(
            text = "Войти",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Войдите в существующий аккаунт",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(Modifier.height(40.dp))

        AuthTextField(
            value = state.email,
            onValueChange = { onEvent(AuthEvent.UpdateEmail(it)) },
            placeholder = "Почта",
            isError = state.emailError != null,
            errorText = state.emailError,
            keyboardType = KeyboardType.Email
        )

        Spacer(Modifier.height(12.dp))

        AuthTextField(
            value = state.password,
            onValueChange = { onEvent(AuthEvent.UpdatePassword(it)) },
            placeholder = "Пароль",
            isPassword = true,
            isPasswordVisible = state.isPasswordVisible,
            onTogglePassword = { onEvent(AuthEvent.TogglePasswordVisibility) },
            isError = state.passwordError != null,
            errorText = state.passwordError
        )

        if (state.error != null) {
            Text(
                text = state.error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        AuthButton(
            text = "Войти",
            isLoading = state.isLoading,
            onClick = { onEvent(AuthEvent.LoginSubmit) }
        )

        Spacer(Modifier.height(24.dp))

        ClickableText(
            primary = "Еще нет аккаунта? ",
            action = "Зарегистрироваться",
            onClick = { onEvent(AuthEvent.GoToRegister) }
        )
    }
}


@Composable
private fun RegisterStep1Screen(state: AuthState, onEvent: (AuthEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(60.dp))

        Text(
            text = "Регистрация",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Создайте новый аккаунт",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(Modifier.height(32.dp))

        AuthTextField(
            value = state.fullName,
            onValueChange = { onEvent(AuthEvent.UpdateFullName(it)) },
            placeholder = "Имя, фамилия *",
//            leadingIcon = Icons.Default.Person,
            isError = state.fullNameError != null,
            errorText = state.fullNameError
        )
        Spacer(Modifier.height(12.dp))

        AuthTextField(
            value = state.email,
            onValueChange = { onEvent(AuthEvent.UpdateEmail(it)) },
            placeholder = "Почта *",
//            leadingIcon = Icons.Default.Email,
            isError = state.emailError != null,
            errorText = state.emailError,
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(12.dp))

        AuthTextField(
            value = state.phone,
            onValueChange = { onEvent(AuthEvent.UpdatePhone(it)) },
            placeholder = "Номер телефона *",
//            leadingIcon = Icons.Default.Phone,
            isError = state.phoneError != null,
            errorText = state.phoneError,
            keyboardType = KeyboardType.Phone
        )
        Spacer(Modifier.height(12.dp))

        AuthTextField(
            value = state.password,
            onValueChange = { onEvent(AuthEvent.UpdatePassword(it)) },
            placeholder = "Пароль *",
//            leadingIcon = Icons.Default.Lock,
            isPassword = true,
            isPasswordVisible = state.isPasswordVisible,
            onTogglePassword = { onEvent(AuthEvent.TogglePasswordVisibility) },
            isError = state.passwordError != null,
            errorText = state.passwordError
        )
        Spacer(Modifier.height(12.dp))

        AuthTextField(
            value = state.confirmPassword,
            onValueChange = { onEvent(AuthEvent.UpdateConfirmPassword(it)) },
            placeholder = "Подтвердите пароль *",
//            leadingIcon = Icons.Default.Lock,
            isPassword = true,
            isPasswordVisible = state.isPasswordVisible,
            isError = state.confirmPasswordError != null,
            errorText = state.confirmPasswordError
        )

        Text(
            text = "Пароль должен содержать не менее 8 символов",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 6.dp).align(Alignment.Start)
        )

        if (state.error != null) {
            Text(
                text = state.error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(Modifier.height(32.dp))

        AuthButton(
            text = "Далее",
            isLoading = false,
            onClick = { onEvent(AuthEvent.RegisterStep1Next) }
        )

        Spacer(Modifier.height(16.dp))

        ClickableText(
            primary = "Уже есть аккаунт? ",
            action = "Войти",
            onClick = { onEvent(AuthEvent.GoToLogin) }
        )

        Spacer(Modifier.height(32.dp))
    }
}


@Composable
private fun RegisterStep2Screen(state: AuthState, onEvent: (AuthEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        // Шапка с кнопкой назад
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            /*IconButton(onClick = { onEvent(AuthEvent.BackToStep1) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
            }*/
            Text(
                text = "Персональная информация",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )
        }

        Spacer(Modifier.height(24.dp))

        AuthTextField(
            value = state.contactInfo,
            onValueChange = { onEvent(AuthEvent.UpdateContactInfo(it)) },
            placeholder = "Социальные сети",
//            leadingIcon = Icons.Default.List,
            supportingText = "Ссылки на Telegram, LinkedIn или VK"
        )

        Spacer(Modifier.height(16.dp))

        AuthTextField(
            value = state.portfolioLink,
            onValueChange = { onEvent(AuthEvent.UpdatePortfolioLink(it)) },
            placeholder = "Портфолио",
//            leadingIcon = Icons.Default.Link,
            supportingText = "Ссылка на примеры ваших работ"
        )

        Spacer(Modifier.height(16.dp))

        AuthTextField(
            value = state.skills,
            onValueChange = { onEvent(AuthEvent.UpdateSkills(it)) },
            placeholder = "Ключевые навыки",
//            leadingIcon = Icons.Default.Work,
            supportingText = "Например: Git, Java, Продажи, Английский C1"
        )

        if (state.error != null) {
            Text(
                text = state.error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onEvent(AuthEvent.TogglePersonalDataConsent) }
        ) {
            Checkbox(
                checked = state.isPersonalDataConsentChecked,
                onCheckedChange = { onEvent(AuthEvent.TogglePersonalDataConsent) }
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = buildAnnotatedString {
                    append("Согласен с ")
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("политикой обработки персональных данных")
                    }
                },
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (state.consentError != null) {
            Text(state.consentError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
        }

        AuthButton(
            text = "Зарегистрироваться",
            isLoading = state.isLoading,
            onClick = { onEvent(AuthEvent.RegisterSubmit) },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ClickableText(
            primary = "Уже есть аккаунт? ",
            action = "Войти",
            onClick = { onEvent(AuthEvent.GoToLogin) },
            modifier = Modifier.padding(bottom = 40.dp)
        )
    }
}