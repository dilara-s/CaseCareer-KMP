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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import org.koin.compose.viewmodel.koinViewModel
import ru.kpfu.itis.R
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
                AuthEffect.NavigateToMain -> navController.navigate("feed") {
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
    LaunchedEffect(Unit) { Firebase.analytics.logEvent("launch_login", null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(80.dp))

        Text(
            text = stringResource(R.string.login_title),
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = stringResource(R.string.login_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(Modifier.height(40.dp))

        AuthTextField(
            value = state.email,
            onValueChange = { onEvent(AuthEvent.UpdateEmail(it)) },
            placeholder = stringResource(R.string.login_email_placeholder),
            isError = state.emailError != null,
            errorText = state.emailError,
            keyboardType = KeyboardType.Email
        )

        Spacer(Modifier.height(12.dp))

        AuthTextField(
            value = state.password,
            onValueChange = { onEvent(AuthEvent.UpdatePassword(it)) },
            placeholder = stringResource(R.string.login_password_placeholder),
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
            text = stringResource(R.string.login_button),
            isLoading = state.isLoading,
            onClick = { onEvent(AuthEvent.LoginSubmit) }
        )

        Spacer(Modifier.height(24.dp))

        ClickableText(
            primary = stringResource(R.string.login_no_account),
            action = stringResource(R.string.login_go_register),
            onClick = { onEvent(AuthEvent.GoToRegister) }
        )
    }
}


@Composable
private fun RegisterStep1Screen(state: AuthState, onEvent: (AuthEvent) -> Unit) {
    LaunchedEffect(Unit) { Firebase.analytics.logEvent("launch_register_step1", null) }
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
            text = stringResource(R.string.register_title),
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = stringResource(R.string.register_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(Modifier.height(32.dp))

        AuthTextField(
            value = state.fullName,
            onValueChange = { onEvent(AuthEvent.UpdateFullName(it)) },
            placeholder = stringResource(R.string.register_full_name_placeholder),
            isError = state.fullNameError != null,
            errorText = state.fullNameError
        )
        Spacer(Modifier.height(12.dp))

        AuthTextField(
            value = state.email,
            onValueChange = { onEvent(AuthEvent.UpdateEmail(it)) },
            placeholder = stringResource(R.string.register_email_placeholder),
            isError = state.emailError != null,
            errorText = state.emailError,
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(12.dp))

        AuthTextField(
            value = state.phone,
            onValueChange = { onEvent(AuthEvent.UpdatePhone(it)) },
            placeholder = stringResource(R.string.register_phone_placeholder),
            isError = state.phoneError != null,
            errorText = state.phoneError,
            keyboardType = KeyboardType.Phone
        )
        Spacer(Modifier.height(12.dp))

        AuthTextField(
            value = state.password,
            onValueChange = { onEvent(AuthEvent.UpdatePassword(it)) },
            placeholder = stringResource(R.string.register_password_placeholder),
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
            placeholder = stringResource(R.string.register_confirm_password_placeholder),
            isPassword = true,
            isPasswordVisible = state.isPasswordVisible,
            isError = state.confirmPasswordError != null,
            errorText = state.confirmPasswordError
        )

        Text(
            text = stringResource(R.string.register_password_hint),
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
            text = stringResource(R.string.register_next_button),
            isLoading = false,
            onClick = { onEvent(AuthEvent.RegisterStep1Next) }
        )

        Spacer(Modifier.height(16.dp))

        ClickableText(
            primary = stringResource(R.string.register_has_account),
            action = stringResource(R.string.register_go_login),
            onClick = { onEvent(AuthEvent.GoToLogin) }
        )

        Spacer(Modifier.height(32.dp))
    }
}


@Composable
private fun RegisterStep2Screen(state: AuthState, onEvent: (AuthEvent) -> Unit) {
    LaunchedEffect(Unit) { Firebase.analytics.logEvent("launch_register_step2", null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.register_personal_info_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )
        }

        Spacer(Modifier.height(24.dp))

        AuthTextField(
            value = state.contactInfo,
            onValueChange = { onEvent(AuthEvent.UpdateContactInfo(it)) },
            placeholder = stringResource(R.string.register_social_placeholder),
            supportingText = stringResource(R.string.register_social_hint)
        )

        Spacer(Modifier.height(16.dp))

        AuthTextField(
            value = state.portfolioLink,
            onValueChange = { onEvent(AuthEvent.UpdatePortfolioLink(it)) },
            placeholder = stringResource(R.string.register_portfolio_placeholder),
            supportingText = stringResource(R.string.register_portfolio_hint)
        )

        Spacer(Modifier.height(16.dp))

        AuthTextField(
            value = state.skills,
            onValueChange = { onEvent(AuthEvent.UpdateSkills(it)) },
            placeholder = stringResource(R.string.register_skills_placeholder),
            supportingText = stringResource(R.string.register_skills_hint)
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
                    append(stringResource(R.string.register_consent_prefix))
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(stringResource(R.string.register_consent_link))
                    }
                },
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (state.consentError != null) {
            Text(state.consentError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
        }

        AuthButton(
            text = stringResource(R.string.register_button),
            isLoading = state.isLoading,
            onClick = { onEvent(AuthEvent.RegisterSubmit) },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ClickableText(
            primary = stringResource(R.string.register_has_account),
            action = stringResource(R.string.register_go_login),
            onClick = { onEvent(AuthEvent.GoToLogin) },
            modifier = Modifier.padding(bottom = 40.dp)
        )
    }
}
