import SwiftUI
import Shared
import FirebaseAnalytics

struct ResponseFormView: View {
    let caseId: Int32
    let caseTitle: String
    let companyName: String
    let ndaRequired: Bool
    let onClose: () -> Void
    let onNavigateToMyCases: () -> Void

    @StateObject private var wrapper = ResponseViewModelWrapper()

    var body: some View {
        let state = wrapper.state

        NavigationStack {
            VStack(spacing: 0) {

                // Прогресс-бар (3 сегмента если NDA, 2 если нет)
                ResponseProgressBar(
                    currentStep: currentStep(state),
                    totalSteps: Int(state.totalSteps)
                )
                .padding(.horizontal, 16)
                .padding(.top, 12)
                .padding(.bottom, 20)

                // Контент
                Group {
                    switch state.screenMode {
                    case ResponseScreenMode.ndastep:
                        NdaStepView(
                            companyName: state.companyName,
                            isAccepted: state.isNdaAccepted,
                            error: state.ndaError,
                            onToggle: { wrapper.onEvent(ResponseEvent.ToggleNdaAccepted()) },
                            onProceed: { wrapper.onEvent(ResponseEvent.ProceedFromNda()) },
                            onCancel: { wrapper.onEvent(ResponseEvent.Cancel()) }
                        )

                    case ResponseScreenMode.formstep:
                        FormStepView(
                            coverLetter: state.coverLetter,
                            coverLetterError: state.coverLetterError,
                            solutionLink: state.solutionLink,
                            solutionLinkError: state.solutionLinkError,
                            isLoading: state.isLoading,
                            error: state.error,
                            onCoverLetterChange: { wrapper.onEvent(ResponseEvent.UpdateCoverLetter(text: $0)) },
                            onSolutionLinkChange: { wrapper.onEvent(ResponseEvent.UpdateSolutionLink(link: $0)) },
                            onSubmit: { wrapper.onEvent(ResponseEvent.Submit()) },
                            onCancel: { wrapper.onEvent(ResponseEvent.Cancel()) }
                        )

                    case ResponseScreenMode.successstep:
                        SuccessStepView(
                            companyName: state.companyName,
                            submittedAt: state.submittedAt ?? "",
                            responseStatus: state.responseStatus ?? "",
                            onBackToFeed: { wrapper.onEvent(ResponseEvent.BackToFeed()) }
                        )

                    default:
                        EmptyView()
                    }
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
            }
            .navigationTitle(navTitle(state))
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                if state.screenMode != ResponseScreenMode.successstep {
                    ToolbarItem(placement: .navigationBarLeading) {
                        Button {
                            wrapper.onEvent(ResponseEvent.Cancel())
                        } label: {
                            Image(systemName: "xmark")
                                .foregroundColor(.primary)
                        }
                    }
                }
            }
        }
        .onAppear {
            Analytics.logEvent("launch_response_form", parameters: nil)
            wrapper.onEvent(ResponseEvent.Init(
                caseId: caseId,
                caseTitle: caseTitle,
                companyName: companyName,
                ndaRequired: ndaRequired
            ))
            wrapper.collectEffects(
                onClose: onClose,
                onNavigateToMyCases: onNavigateToMyCases
            )
        }
    }

    // MARK: - Helpers

    private func navTitle(_ state: ResponseState) -> String {
        switch state.screenMode {
        case ResponseScreenMode.ndastep:    return "Соглашение NDA"
        case ResponseScreenMode.formstep:   return "Отклик на кейс"
        case ResponseScreenMode.successstep: return "Отклик на кейс"
        default: return "Отклик"
        }
    }

    private func currentStep(_ state: ResponseState) -> Int {
        switch state.screenMode {
        case ResponseScreenMode.ndastep:     return 1
        case ResponseScreenMode.formstep:    return state.isNdaRequired ? 2 : 1
        case ResponseScreenMode.successstep: return Int(state.totalSteps)
        default: return 1
        }
    }
}

// MARK: - Progress Bar

private struct ResponseProgressBar: View {
    let currentStep: Int
    let totalSteps: Int

    var body: some View {
        HStack(spacing: 6) {
            ForEach(1...max(totalSteps, 1), id: \.self) { step in
                Capsule()
                    .fill(step <= currentStep ? Color.brandPrimary : Color(.systemGray5))
                    .frame(height: 4)
                    .animation(.easeInOut(duration: 0.2), value: currentStep)
            }
        }
    }
}

// MARK: - Step 1: NDA

private struct NdaStepView: View {
    let companyName: String
    let isAccepted: Bool
    let error: String?
    let onToggle: () -> Void
    let onProceed: () -> Void
    let onCancel: () -> Void

    // Локальный второй чекбокс (обязательство о неразглашении)
    @State private var obligation = false

    // Обе галки нужны для продолжения
    private var bothAccepted: Bool { isAccepted && obligation }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 24) {

                // Иконка NDA
                HStack {
                    ZStack {
                        RoundedRectangle(cornerRadius: 16)
                            .fill(Color.ndaRequiredBg)
                            .frame(width: 64, height: 64)
                        Image(systemName: "lock.fill")
                            .font(.system(size: 28))
                            .foregroundColor(Color.ndaRequiredText)
                    }
                    Spacer()
                }

                // Заголовок + описание
                VStack(alignment: .leading, spacing: 8) {
                    Text("Этот кейс требует NDA")
                        .font(.dsDisplaySmall)
                    Text("Перед откликом ознакомьтесь с условиями соглашения о неразглашении и подтвердите согласие.")
                        .font(.dsBodySmall)
                        .foregroundColor(.onSurfaceVariant)
                }

                // Ссылка на документ NDA
                Button {} label: {
                    HStack(spacing: 6) {
                        Image(systemName: "info.circle")
                            .font(.system(size: 14))
                        Text("Соглашение о неразглашении (NDA)")
                            .font(.dsLabelMedium)
                        Image(systemName: "chevron.right")
                            .font(.system(size: 12))
                    }
                    .foregroundColor(.brandPrimary)
                }

                // Стороны соглашения
                HStack(spacing: 0) {
                    // Заказчик
                    VStack(alignment: .leading, spacing: 6) {
                        Text("Заказчик")
                            .font(.dsCaption)
                            .foregroundColor(.onSurfaceVariant)
                        HStack(spacing: 8) {
                            ZStack {
                                Circle()
                                    .fill(Color.brandBackground)
                                    .frame(width: 28, height: 28)
                                Text(String(companyName.prefix(1)))
                                    .font(.dsLabelSmall)
                                    .foregroundColor(.brandPrimary)
                            }
                            Text(companyName)
                                .font(.dsLabelMedium)
                                .lineLimit(1)
                        }
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)

                    Divider()
                        .frame(height: 44)
                        .padding(.horizontal, 12)

                    // Исполнитель
                    VStack(alignment: .leading, spacing: 6) {
                        Text("Исполнитель")
                            .font(.dsCaption)
                            .foregroundColor(.onSurfaceVariant)
                        HStack(spacing: 8) {
                            ZStack {
                                Circle()
                                    .fill(Color(.systemGray5))
                                    .frame(width: 28, height: 28)
                                Image(systemName: "person.fill")
                                    .font(.system(size: 12))
                                    .foregroundColor(.secondary)
                            }
                            Text("Вы")
                                .font(.dsLabelMedium)
                        }
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                }
                .padding(14)
                .background(Color.surfaceCard)
                .cornerRadius(12)
                .overlay(
                    RoundedRectangle(cornerRadius: 12)
                        .stroke(Color.outline, lineWidth: 1)
                )

                // Чекбоксы
                VStack(alignment: .leading, spacing: 16) {
                    DSCheckbox(
                        label: "Я прочитал(а) и согласен(на) с условиями Соглашения о неразглашении",
                        isChecked: isAccepted,
                        error: error != nil && !isAccepted,
                        onToggle: onToggle
                    )

                    DSCheckbox(
                        label: "Обязуюсь не передавать полученные данные третьим лицам",
                        isChecked: obligation,
                        error: error != nil && !obligation,
                        onToggle: { obligation.toggle() }
                    )
                }

                // Ошибка валидации
                if let error = error {
                    Text(error)
                        .font(.dsCaption)
                        .foregroundColor(.errorRed)
                }

                Spacer().frame(height: 8)

                // Кнопки
                VStack(spacing: 12) {
                    PrimaryButton(
                        "Подписать и продолжить",
                        isDisabled: !bothAccepted,
                        action: onProceed
                    )

                    SecondaryButton("Отмена", action: onCancel)
                }
            }
            .padding(16)
        }
    }
}

// MARK: - Step 2: Form

private struct FormStepView: View {
    let coverLetter: String
    let coverLetterError: String?
    let solutionLink: String
    let solutionLinkError: String?
    let isLoading: Bool
    let error: String?
    let onCoverLetterChange: (String) -> Void
    let onSolutionLinkChange: (String) -> Void
    let onSubmit: () -> Void
    let onCancel: () -> Void

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 24) {

                // Сопроводительное письмо
                VStack(alignment: .leading, spacing: 8) {
                    Text("Сопроводительное письмо")
                        .font(.dsLabelMedium)
                        .foregroundColor(.onSurfaceVariant)
                    AppTextArea(
                        placeholder: "Расскажите о своём опыте, релевантном этому кейсу. Почему именно вы справитесь с задачей?",
                        text: Binding(
                            get: { coverLetter },
                            set: { onCoverLetterChange($0) }
                        ),
                        error: coverLetterError,
                        minHeight: 130
                    )
                }

                // Ссылка на решение
                VStack(alignment: .leading, spacing: 8) {
                    Text("Ссылка на решение")
                        .font(.dsLabelMedium)
                        .foregroundColor(.onSurfaceVariant)
                    AppTextField(
                        placeholder: "GitHub, Notion, Google Drive и др.",
                        text: Binding(
                            get: { solutionLink },
                            set: { onSolutionLinkChange($0) }
                        ),
                        error: solutionLinkError,
                        keyboardType: .URL,
                        leadingIcon: "link"
                    )
                }

                // Глобальная ошибка
                if let error = error {
                    HStack(spacing: 8) {
                        Image(systemName: "exclamationmark.circle.fill")
                            .foregroundColor(.errorRed)
                        Text(error)
                            .font(.dsBodySmall)
                            .foregroundColor(.errorRed)
                    }
                    .padding(12)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .background(Color.errorRed.opacity(0.08))
                    .cornerRadius(10)
                }

                Spacer().frame(height: 8)

                // Кнопки
                VStack(spacing: 12) {
                    PrimaryButton("Отправить отклик", isLoading: isLoading, action: onSubmit)
                    SecondaryButton("Отмена", action: onCancel)
                }
            }
            .padding(16)
        }
    }
}

// MARK: - Step 3: Success

private struct SuccessStepView: View {
    let companyName: String
    let submittedAt: String
    let responseStatus: String
    let onBackToFeed: () -> Void

    private var displayStatus: String {
        switch responseStatus.lowercased() {
        case "pending", "in_review", "review": return "На проверке"
        case "accepted", "approved":           return "Принято"
        case "rejected":                       return "Отклонено"
        default:                               return responseStatus.isEmpty ? "На проверке" : responseStatus
        }
    }

    var body: some View {
        VStack(spacing: 0) {
            Spacer()

            // Иконка успеха
            ZStack {
                Circle()
                    .fill(Color.successGreen.opacity(0.15))
                    .frame(width: 96, height: 96)
                Image(systemName: "checkmark.circle.fill")
                    .font(.system(size: 52))
                    .foregroundColor(.successGreen)
            }

            Spacer().frame(height: 24)

            // Заголовок
            VStack(spacing: 8) {
                Text("Отклик отправлен!")
                    .font(.dsDisplaySmall)

                Text("\(companyName) получила ваше решение и письмо. Следите за статусом в разделе «Мои кейсы».")
                    .font(.dsBodySmall)
                    .foregroundColor(.onSurfaceVariant)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 24)
            }

            Spacer().frame(height: 32)

            // Статус-карточка
            VStack(alignment: .leading, spacing: 16) {
                if !submittedAt.isEmpty {
                    BulletStatusRow(
                        title: "Отправлено",
                        subtitle: submittedAt,
                        color: .successGreen
                    )
                }
                BulletStatusRow(
                    title: "Статус: \(displayStatus)",
                    subtitle: "Компания рассматривает отклик",
                    color: .brandPrimary
                )
            }
            .padding(16)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(Color.surfaceCard)
            .cornerRadius(16)
            .shadow(color: .black.opacity(0.05), radius: 4, x: 0, y: 2)
            .padding(.horizontal, 24)

            Spacer()

            // Кнопка
            PrimaryButton("Вернуться к ленте", action: onBackToFeed)
                .padding(.horizontal, 24)
                .padding(.bottom, 24)
        }
    }
}
