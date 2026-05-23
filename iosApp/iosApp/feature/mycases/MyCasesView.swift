import SwiftUI
import Shared

struct MyCasesView: View {
    @StateObject private var wrapper = MyCasesViewModelWrapper()
    @State private var selectedCaseId: Int64? = nil

    var body: some View {
        let state = wrapper.state

        Group {
            if state.isLoading {
                ProgressView()
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
            } else if let error = state.error {
                ErrorStateView(message: error) {
                    wrapper.onEvent(MyCasesEvent.Load())
                }
            } else if state.cases.isEmpty {
                EmptyStateView(
                    icon: "heart",
                    title: "Пока нет откликов",
                    subtitle: "Здесь появятся кейсы,\nна которые вы откликнулись"
                )
            } else {
                casesList(cases: state.cases)
            }
        }
        .navigationTitle("Мои отклики")
        .navigationBarTitleDisplayMode(.large)
        .background(Color.surfaceBackground.ignoresSafeArea())
        .navigationDestination(item: $selectedCaseId) { caseId in
            CaseDetailView(caseId: caseId)
        }
        .onAppear {
            wrapper.onEvent(MyCasesEvent.Load())
            wrapper.collectEffects { caseId in
                selectedCaseId = caseId
            }
        }
    }

    private func casesList(cases: [MyCase]) -> some View {
        ScrollView {
            LazyVStack(spacing: 12) {
                ForEach(cases, id: \.caseId) { myCase in
                    CaseResponseCard(myCase: myCase)
                        .onTapGesture {
                            wrapper.onEvent(MyCasesEvent.OpenCase(caseId: myCase.caseId))
                        }
                }
            }
            .padding(16)
        }
        .refreshable {
            wrapper.onEvent(MyCasesEvent.Refresh())
        }
    }
}

// MARK: - Case Response Card

private struct CaseResponseCard: View {
    let myCase: MyCase

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            HStack(alignment: .top) {
                VStack(alignment: .leading, spacing: 4) {
                    StatusBadge(status: myCase.status)
                    Text(myCase.title)
                        .font(.dsHeadlineSmall)
                        .lineLimit(2)
                        .fixedSize(horizontal: false, vertical: true)
                    Text(myCase.companyName)
                        .font(.dsBodySmall)
                        .foregroundColor(.onSurfaceVariant)
                }
                Spacer(minLength: 8)
            }

            HStack(spacing: 6) {
                Image(systemName: "clock")
                    .font(.system(size: 12))
                    .foregroundColor(.onSurfaceVariant)
                Text("Отклик отправлен \(Formatters.shortDate(myCase.submittedAt))")
                    .font(.dsCaption)
                    .foregroundColor(.onSurfaceVariant)
            }
        }
        .padding(16)
        .background(Color.surfaceCard)
        .cornerRadius(16)
        .shadow(color: .black.opacity(0.06), radius: 4, x: 0, y: 2)
    }
}
