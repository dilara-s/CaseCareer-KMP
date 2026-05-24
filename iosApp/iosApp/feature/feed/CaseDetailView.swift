import SwiftUI
import Shared
import FirebaseAnalytics

struct CaseDetailView: View {
    let caseId: Int64
    var onBackToRoot: (() -> Void)? = nil

    @StateObject private var wrapper: CaseDetailViewModelWrapper
    @State private var showResponseSheet = false
    @Environment(\.dismiss) private var dismiss

    init(caseId: Int64, onBackToRoot: (() -> Void)? = nil) {
        self.caseId = caseId
        self.onBackToRoot = onBackToRoot
        _wrapper = StateObject(wrappedValue: CaseDetailViewModelWrapper(caseId: caseId))
    }

    var body: some View {
        let state = wrapper.state

        Group {
            if state.isLoading {
                ProgressView()
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
            } else if let error = state.error {
                ErrorStateView(message: error) {
                    wrapper.onEvent(CaseDetailEvent.Retry())
                }
            } else if let caseDetail = state.case_ {
                detailContent(caseDetail: caseDetail)
            }
        }
        .background(Color.surfaceBackground.ignoresSafeArea())
        .navigationBarTitleDisplayMode(.inline)
        .sheet(isPresented: $showResponseSheet) {
            if let caseDetail = wrapper.state.case_ {
                ResponseFormView(
                    caseId: Int32(caseDetail.id),
                    caseTitle: caseDetail.title,
                    companyName: caseDetail.companyName,
                    ndaRequired: caseDetail.ndaRequired,
                    onClose: { showResponseSheet = false },
                    onNavigateToMyCases: {
                        showResponseSheet = false
                        onBackToRoot?()
                    }
                )
            }
        }
        .onAppear {
            Analytics.logEvent("launch_case_detail", parameters: ["case_id": caseId])
            wrapper.collectEffects(
                onNavigateBack: { dismiss() },
                onNavigateToNda: { _ in showResponseSheet = true },
                onNavigateToApply: { _ in showResponseSheet = true }
            )
        }
    }

    @ViewBuilder
    private func detailContent(caseDetail: CaseDetail) -> some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 0) {

                // Шапка
                VStack(alignment: .leading, spacing: 12) {
                    // NDA + статус
                    HStack {
                        NdaBadge(ndaRequired: caseDetail.ndaRequired)
                        Spacer()
                        StatusBadge(status: caseDetail.status)
                    }

                    // Заголовок
                    Text(caseDetail.title)
                        .font(.dsDisplaySmall)
                        .fixedSize(horizontal: false, vertical: true)

                    // Вознаграждение
                    Text(Formatters.reward(caseDetail.reward))
                        .font(.system(size: 24, weight: .bold))
                        .foregroundColor(.primary)

                    // Дедлайн + NDA метка
                    HStack(spacing: 12) {
                        Label("до \(Formatters.deadline(caseDetail.deadline))", systemImage: "calendar")
                            .font(.dsBodySmall)
                            .foregroundColor(.onSurfaceVariant)

                        if caseDetail.ndaRequired {
                            Label("NDA требуется", systemImage: "lock.fill")
                                .font(.dsBodySmall)
                                .foregroundColor(.ndaRequiredText)
                        }
                    }
                }
                .padding(16)
                .background(Color.surfaceCard)

                Divider()

                // Компания
                HStack(spacing: 12) {
                    ZStack {
                        RoundedRectangle(cornerRadius: 8)
                            .fill(Color.brandBackground)
                            .frame(width: 40, height: 40)
                        Text(String(caseDetail.companyName.prefix(1)))
                            .font(.dsHeadlineMedium)
                            .foregroundColor(.brandPrimary)
                    }
                    VStack(alignment: .leading, spacing: 2) {
                        Text(caseDetail.companyName)
                            .font(.dsHeadlineSmall)
                        Text("Опубликовано \(Formatters.shortDate(caseDetail.publishedAt))")
                            .font(.dsCaption)
                            .foregroundColor(.onSurfaceVariant)
                    }
                    Spacer()
                }
                .padding(16)
                .background(Color.surfaceCard)

                Divider()

                // Описание
                VStack(alignment: .leading, spacing: 10) {
                    SectionLabel(title: "Описание")
                    Text(caseDetail.description_)
                        .font(.dsBodyLarge)
                        .lineSpacing(4)
                        .fixedSize(horizontal: false, vertical: true)
                }
                .padding(16)
                .frame(maxWidth: .infinity, alignment: .leading)
                .background(Color.surfaceCard)

                Divider()

                // Информация
                VStack(alignment: .leading, spacing: 12) {
                    SectionLabel(title: "Информация")
                    InfoRow(label: "Опубликовано", value: Formatters.shortDate(caseDetail.publishedAt))
                    InfoRow(label: "Создано", value: Formatters.shortDate(caseDetail.createdAt))
                    InfoRow(label: "Статус", value: caseDetail.status)
                }
                .padding(16)
                .background(Color.surfaceCard)

                Divider()

                // Кнопка
                PrimaryButton("Откликнуться") {
                    wrapper.onEvent(CaseDetailEvent.Apply())
                }
                .padding(16)
                .background(Color.surfaceCard)
            }
        }
        .navigationTitle(caseDetail.companyName)
    }
}
