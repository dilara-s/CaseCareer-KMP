import SwiftUI
import Shared

struct AuthView: View {
    let onNavigateToMain: () -> Void

    @StateObject private var wrapper = AuthViewModelWrapper()

    var body: some View {
        let state = wrapper.state

        NavigationStack {
            Group {
                switch state.screenMode {
                case AuthScreenMode.login:
                    LoginScreen(state: state, onEvent: wrapper.onEvent)
                        .navigationTitle("")
                        .navigationBarHidden(true)

                case AuthScreenMode.registerstep1:
                    RegisterStep1Screen(state: state, onEvent: wrapper.onEvent)
                        .navigationTitle("")
                        .navigationBarHidden(true)

                case AuthScreenMode.registerstep2:
                    RegisterStep2Screen(state: state, onEvent: wrapper.onEvent)
                        .navigationTitle("Персональная информация")
                        .navigationBarTitleDisplayMode(.inline)
                        .toolbar {
                            ToolbarItem(placement: .navigationBarLeading) {
                                Button {
                                    wrapper.onEvent(AuthEvent.BackToStep1())
                                } label: {
                                    Image(systemName: "chevron.left")
                                        .foregroundColor(.primary)
                                }
                            }
                        }

                default:
                    LoginScreen(state: state, onEvent: wrapper.onEvent)
                        .navigationBarHidden(true)
                }
            }
        }
        .onAppear {
            wrapper.collectEffects(onNavigateToMain: onNavigateToMain)
        }
    }
}

// MARK: - Login

private struct LoginScreen: View {
    let state: AuthState
    let onEvent: (AuthEvent) -> Void

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 0) {
                Spacer().frame(height: 80)

                VStack(alignment: .leading, spacing: 8) {
                    Text("Войти")
                        .font(.dsDisplayLarge)
                    Text("Войдите в существующий аккаунт")
                        .font(.dsBodySmall)
                        .foregroundColor(.onSurfaceVariant)
                }
                .frame(maxWidth: .infinity, alignment: .leading)

                Spacer().frame(height: 40)

                VStack(spacing: 14) {
                    AppTextField(
                        placeholder: "Почта",
                        text: Binding(
                            get: { state.email },
                            set: { onEvent(AuthEvent.UpdateEmail(email: $0)) }
                        ),
                        error: state.emailError,
                        keyboardType: .emailAddress,
                        leadingIcon: "envelope"
                    )

                    AppTextField(
                        placeholder: "Пароль",
                        text: Binding(
                            get: { state.password },
                            set: { onEvent(AuthEvent.UpdatePassword(password: $0)) }
                        ),
                        error: state.passwordError,
                        leadingIcon: "lock",
                        isPassword: true
                    )
                }

                if let error = state.error {
                    Text(error)
                        .font(.dsCaption)
                        .foregroundColor(.errorRed)
                        .padding(.top, 8)
                }

                Spacer().frame(height: 28)

                PrimaryButton("Войти", isLoading: state.isLoading) {
                    onEvent(AuthEvent.LoginSubmit())
                }

                Spacer().frame(height: 24)

                ClickableText(
                    primary: "Ещё нет аккаунта? ",
                    action: "Зарегистрироваться",
                    onClick: { onEvent(AuthEvent.GoToRegister()) }
                )
                .frame(maxWidth: .infinity)

                Spacer().frame(height: 40)
            }
            .padding(.horizontal, 24)
        }
        .background(Color.surfaceBackground.ignoresSafeArea())
    }
}

// MARK: - Register Step 1

private struct RegisterStep1Screen: View {
    let state: AuthState
    let onEvent: (AuthEvent) -> Void

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 0) {
                Spacer().frame(height: 60)

                VStack(alignment: .leading, spacing: 8) {
                    Text("Регистрация")
                        .font(.dsDisplayLarge)
                    Text("Создайте новый аккаунт")
                        .font(.dsBodySmall)
                        .foregroundColor(.onSurfaceVariant)
                }

                Spacer().frame(height: 32)

                VStack(spacing: 14) {
                    AppTextField(
                        placeholder: "Имя, Фамилия *",
                        text: Binding(
                            get: { state.fullName },
                            set: { onEvent(AuthEvent.UpdateFullName(name: $0)) }
                        ),
                        error: state.fullNameError,
                        leadingIcon: "person"
                    )

                    AppTextField(
                        placeholder: "Почта *",
                        text: Binding(
                            get: { state.email },
                            set: { onEvent(AuthEvent.UpdateEmail(email: $0)) }
                        ),
                        error: state.emailError,
                        keyboardType: .emailAddress,
                        leadingIcon: "envelope"
                    )

                    AppTextField(
                        placeholder: "Номер телефона *",
                        text: Binding(
                            get: { state.phone },
                            set: { onEvent(AuthEvent.UpdatePhone(phone: $0)) }
                        ),
                        error: state.phoneError,
                        keyboardType: .phonePad,
                        leadingIcon: "phone"
                    )

                    AppTextField(
                        placeholder: "Пароль *",
                        text: Binding(
                            get: { state.password },
                            set: { onEvent(AuthEvent.UpdatePassword(password: $0)) }
                        ),
                        error: state.passwordError,
                        leadingIcon: "lock",
                        isPassword: true
                    )

                    AppTextField(
                        placeholder: "Подтвердите пароль *",
                        text: Binding(
                            get: { state.confirmPassword },
                            set: { onEvent(AuthEvent.UpdateConfirmPassword(confirm: $0)) }
                        ),
                        error: state.confirmPasswordError,
                        leadingIcon: "lock.rotation",
                        isPassword: true
                    )
                }

                Text("Пароль должен содержать не менее 8 символов")
                    .font(.dsCaption)
                    .foregroundColor(.onSurfaceVariant)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.top, 6)

                if let error = state.error {
                    Text(error)
                        .font(.dsCaption)
                        .foregroundColor(.errorRed)
                        .padding(.top, 8)
                }

                Spacer().frame(height: 32)

                PrimaryButton("Далее") {
                    onEvent(AuthEvent.RegisterStep1Next())
                }

                Spacer().frame(height: 16)

                ClickableText(
                    primary: "Уже есть аккаунт? ",
                    action: "Войти",
                    onClick: { onEvent(AuthEvent.GoToLogin()) }
                )
                .frame(maxWidth: .infinity)

                Spacer().frame(height: 40)
            }
            .padding(.horizontal, 24)
        }
        .background(Color.surfaceBackground.ignoresSafeArea())
    }
}

// MARK: - Register Step 2

private struct RegisterStep2Screen: View {
    let state: AuthState
    let onEvent: (AuthEvent) -> Void

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 0) {
                Spacer().frame(height: 16)

                VStack(spacing: 20) {
                    // Социальные сети
                    VStack(alignment: .leading, spacing: 4) {
                        AppTextField(
                            placeholder: "Социальные сети",
                            text: Binding(
                                get: { state.contactInfo },
                                set: { onEvent(AuthEvent.UpdateContactInfo(info: $0)) }
                            ),
                            leadingIcon: "link"
                        )
                        Text("Ссылки на Telegram, LinkedIn или VK")
                            .font(.dsCaption)
                            .foregroundColor(.onSurfaceVariant)
                            .padding(.horizontal, 4)
                    }

                    // Портфолио
                    VStack(alignment: .leading, spacing: 4) {
                        AppTextField(
                            placeholder: "Портфолио",
                            text: Binding(
                                get: { state.portfolioLink },
                                set: { onEvent(AuthEvent.UpdatePortfolioLink(link: $0)) }
                            ),
                            keyboardType: .URL,
                            leadingIcon: "briefcase"
                        )
                        Text("Ссылка на примеры ваших работ")
                            .font(.dsCaption)
                            .foregroundColor(.onSurfaceVariant)
                            .padding(.horizontal, 4)
                    }

                    // Ключевые навыки
                    VStack(alignment: .leading, spacing: 4) {
                        AppTextField(
                            placeholder: "Ключевые навыки",
                            text: Binding(
                                get: { state.skills },
                                set: { onEvent(AuthEvent.UpdateSkills(skills: $0)) }
                            ),
                            leadingIcon: "wrench.and.screwdriver"
                        )
                        Text("Например: Git, Java, Продажи, Английский C1")
                            .font(.dsCaption)
                            .foregroundColor(.onSurfaceVariant)
                            .padding(.horizontal, 4)
                    }
                }

                Spacer().frame(height: 24)

                // Согласие на обработку данных
                DSCheckbox(
                    label: "Согласен с политикой обработки персональных данных",
                    isChecked: state.isPersonalDataConsentChecked,
                    error: state.consentError != nil,
                    onToggle: { onEvent(AuthEvent.TogglePersonalDataConsent()) }
                )

                if let consentError = state.consentError {
                    Text(consentError)
                        .font(.dsCaption)
                        .foregroundColor(.errorRed)
                        .padding(.top, 4)
                }

                if let error = state.error {
                    Text(error)
                        .font(.dsCaption)
                        .foregroundColor(.errorRed)
                        .padding(.top, 8)
                }

                Spacer().frame(height: 32)

                PrimaryButton("Зарегистрироваться", isLoading: state.isLoading) {
                    onEvent(AuthEvent.RegisterSubmit())
                }

                Spacer().frame(height: 16)

                ClickableText(
                    primary: "Уже есть аккаунт? ",
                    action: "Войти",
                    onClick: { onEvent(AuthEvent.GoToLogin()) }
                )
                .frame(maxWidth: .infinity)

                Spacer().frame(height: 40)
            }
            .padding(.horizontal, 24)
        }
        .background(Color.surfaceBackground.ignoresSafeArea())
    }
}
