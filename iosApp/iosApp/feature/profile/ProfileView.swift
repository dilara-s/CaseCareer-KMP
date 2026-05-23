import SwiftUI
import Shared

struct ProfileView: View {
    let onLogout: () -> Void

    @StateObject private var wrapper = ProfileViewModelWrapper()
    @State private var showDeleteAccountAlert = false

    var body: some View {
        let state = wrapper.state

        Group {
            if state.isLoading && state.profile == nil {
                ProgressView()
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
            } else if let profile = state.profile {
                profileContent(profile: profile)
            } else if let error = state.error {
                ErrorStateView(message: error) {
                    wrapper.onEvent(ProfileEvent.Load())
                }
            }
        }
        .navigationTitle("Профиль")
        .navigationBarTitleDisplayMode(.inline)
        .background(Color.surfaceBackground.ignoresSafeArea())
        .alert("Выйти из аккаунта?", isPresented: Binding(
            get: { state.showLogoutDialog },
            set: { if !$0 { wrapper.onEvent(ProfileEvent.DismissLogoutDialog()) } }
        )) {
            Button("Выйти", role: .destructive) {
                wrapper.onEvent(ProfileEvent.ConfirmLogout())
            }
            Button("Отмена", role: .cancel) {
                wrapper.onEvent(ProfileEvent.DismissLogoutDialog())
            }
        }
        .alert("Удалить аккаунт?", isPresented: $showDeleteAccountAlert) {
            Button("Отмена", role: .cancel) {}
        } message: {
            Text("Функция временно недоступна. Обратитесь в поддержку.")
        }
        .onAppear {
            wrapper.onEvent(ProfileEvent.Load())
            wrapper.collectEffects(onNavigateToAuth: onLogout)
        }
    }

    @ViewBuilder
    private func profileContent(profile: Shared.Profile) -> some View {
        ScrollView {
            VStack(spacing: 0) {

                // Аватар + имя + роль + рейтинг
                VStack(spacing: 0) {
                    HStack(spacing: 16) {
                        // Аватар
                        ZStack {
                            Circle()
                                .fill(Color.brandBackground)
                                .frame(width: 72, height: 72)
                            Text(String(profile.fullName.prefix(1)).uppercased())
                                .font(.system(size: 28, weight: .bold))
                                .foregroundColor(.brandPrimary)
                        }

                        VStack(alignment: .leading, spacing: 6) {
                            Text(profile.fullName)
                                .font(.dsHeadlineLarge)

                            Text(profile.email)
                                .font(.dsBodySmall)
                                .foregroundColor(.onSurfaceVariant)

                            HStack(spacing: 8) {
                                RoleBadge(role: profile.roleType)

                                if !profile.rating.isEmpty {
                                    HStack(spacing: 3) {
                                        Image(systemName: "star.fill")
                                            .font(.system(size: 11))
                                            .foregroundColor(.brandPrimary)
                                        Text("\(profile.rating) рейтинг")
                                            .font(.dsLabelSmall)
                                            .foregroundColor(.brandPrimary)
                                    }
                                }
                            }
                        }
                        Spacer()
                    }
                    .padding(16)
                }
                .background(Color.surfaceCard)

                Divider()

                // Поля профиля
                VStack(alignment: .leading, spacing: 0) {
                    if !profile.contactInfo.isEmpty {
                        profileFieldRow(
                            icon: "link",
                            title: "Социальные сети",
                            value: profile.contactInfo
                        )
                        Divider().padding(.leading, 52)
                    }

                    if !profile.portfolioLink.isEmpty {
                        profileFieldRow(
                            icon: "briefcase",
                            title: "Портфолио",
                            value: profile.portfolioLink
                        )
                        Divider().padding(.leading, 52)
                    }

                    if !profile.skills.isEmpty {
                        skillsRow(skills: profile.skills)
                    }
                }
                .background(Color.surfaceCard)

                Spacer().frame(height: 16)

                // Кнопки
                VStack(spacing: 12) {
                    SecondaryButton("Выйти из приложения") {
                        wrapper.onEvent(ProfileEvent.ShowLogoutDialog())
                    }

                    DestructiveTextButton("Удалить аккаунт") {
                        showDeleteAccountAlert = true
                    }
                }
                .padding(16)
                .background(Color.surfaceCard)
                .cornerRadius(16)
                .padding(.horizontal, 16)

                Spacer().frame(height: 24)
            }
        }
    }

    private func profileFieldRow(icon: String, title: String, value: String) -> some View {
        HStack(alignment: .top, spacing: 16) {
            Image(systemName: icon)
                .font(.system(size: 16))
                .foregroundColor(.brandPrimary)
                .frame(width: 36, height: 36)
                .background(Color.brandBackground)
                .cornerRadius(8)

            VStack(alignment: .leading, spacing: 3) {
                Text(title)
                    .font(.dsCaption)
                    .foregroundColor(.onSurfaceVariant)
                Text(value)
                    .font(.dsBodyMedium)
            }
            Spacer()
        }
        .padding(16)
    }

    private func skillsRow(skills: String) -> some View {
        HStack(alignment: .top, spacing: 16) {
            Image(systemName: "wrench.and.screwdriver")
                .font(.system(size: 16))
                .foregroundColor(.brandPrimary)
                .frame(width: 36, height: 36)
                .background(Color.brandBackground)
                .cornerRadius(8)

            VStack(alignment: .leading, spacing: 8) {
                Text("Ключевые навыки")
                    .font(.dsCaption)
                    .foregroundColor(.onSurfaceVariant)

                FlowLayout(
                    items: skills
                        .components(separatedBy: ",")
                        .map { $0.trimmingCharacters(in: .whitespaces) }
                        .filter { !$0.isEmpty }
                        .reduce(into: [String]()) { result, skill in
                            // Дедупликация с сохранением порядка (без учёта регистра)
                            if !result.contains(where: { $0.lowercased() == skill.lowercased() }) {
                                result.append(skill)
                            }
                        }
                ) { skill in
                    Text(skill)
                        .font(.dsLabelSmall)
                        .padding(.horizontal, 12)
                        .padding(.vertical, 6)
                        .background(Color.brandBackground)
                        .foregroundColor(.brandPrimary)
                        .cornerRadius(8)
                }
            }
            Spacer()
        }
        .padding(16)
    }
}
