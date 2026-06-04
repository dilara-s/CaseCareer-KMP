import SwiftUI
import Shared
import FirebaseAnalytics

struct FeedView: View {
    @StateObject private var wrapper = FeedViewModelWrapper()
    @State private var selectedCaseId: Int64? = nil

    var body: some View {
        let state = wrapper.state

        NavigationStack {
            Group {
                if state.isLoading && state.cases.isEmpty {
                    ProgressView()
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                } else if let error = state.error, state.cases.isEmpty {
                    ErrorStateView(message: error) {
                        wrapper.onEvent(FeedEvent.Refresh())
                    }
                } else if state.cases.isEmpty && !state.isLoading {
                    EmptyStateView(
                        icon: "tray",
                        title: "Кейсов пока нет",
                        subtitle: "Попробуйте зайти позже или измените поисковый запрос"
                    )
                } else {
                    casesList(state: state)
                }
            }
            .navigationTitle("Кейсы")
            .navigationBarTitleDisplayMode(.large)
            .background(Color.surfaceBackground)
            .navigationDestination(item: $selectedCaseId) { caseId in
                CaseDetailView(caseId: caseId, onBackToRoot: { selectedCaseId = nil })
            }
        }
        .onAppear {
            Analytics.logEvent("launch_feed", parameters: nil)
            wrapper.collectEffects(onNavigateToCaseDetail: { caseId in
                selectedCaseId = caseId
            })
        }
    }

    private func casesList(state: FeedState) -> some View {
        ScrollView {
            // Поиск
            FeedSearchBar(
                query: state.searchQuery,
                onQueryChange: { wrapper.onEvent(FeedEvent.SearchQueryChanged(query: $0)) },
                onClear: { wrapper.onEvent(FeedEvent.ClearSearch()) }
            )
            .padding(.horizontal, 16)
            .padding(.top, 12)
            .padding(.bottom, 4)

            // Счётчик результатов поиска
            if !state.searchQuery.isEmpty {
                HStack {
                    Text("Найдено кейсов: \(state.totalCount)")
                        .font(.dsCaption)
                        .foregroundColor(.onSurfaceVariant)
                    Spacer()
                }
                .padding(.horizontal, 16)
                .padding(.bottom, 8)
            }

            LazyVStack(spacing: 12) {
                ForEach(state.cases, id: \.id) { caseItem in
                    CaseFeedCard(caseItem: caseItem) {
                        wrapper.onEvent(FeedEvent.CaseClicked(caseId: caseItem.id))
                    }
                    .padding(.horizontal, 16)
                }

                // Пагинация
                if state.isLoadingMore {
                    ProgressView()
                        .padding()
                } else if state.hasMore && !state.cases.isEmpty {
                    Color.clear.frame(height: 1)
                        .onAppear {
                            wrapper.onEvent(FeedEvent.LoadNextPage())
                        }
                }

                Spacer().frame(height: 16)
            }
            .padding(.top, 8)
        }
        .refreshable {
            wrapper.onEvent(FeedEvent.Refresh())
        }
    }
}

// MARK: - Search Bar

private struct FeedSearchBar: View {
    let query: String
    let onQueryChange: (String) -> Void
    let onClear: () -> Void

    var body: some View {
        HStack(spacing: 8) {
            Image(systemName: "magnifyingglass")
                .foregroundColor(.onSurfaceVariant)
                .font(.system(size: 15))

            ZStack(alignment: .leading) {
                if query.isEmpty {
                    Text("Поиск кейсов")
                        .foregroundColor(.onSurfaceVariant)
                        .font(.system(size: 15))
                }
                TextField("", text: Binding(
                    get: { query },
                    set: { onQueryChange($0) }
                ))
                .font(.system(size: 15))
            }

            if !query.isEmpty {
                Button(action: onClear) {
                    Image(systemName: "xmark.circle.fill")
                        .foregroundColor(.onSurfaceVariant)
                        .font(.system(size: 15))
                }
            }
        }
        .padding(.horizontal, 14)
        .frame(height: 44)
        .background(Color.surfaceCard)
        .cornerRadius(12)
        .shadow(color: .black.opacity(0.05), radius: 2, x: 0, y: 1)
    }
}

// MARK: - Case Feed Card

private struct CaseFeedCard: View {
    let caseItem: Case
    let onTap: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {

            // NDA бейдж
            NdaBadge(ndaRequired: caseItem.ndaRequired)

            // Заголовок
            Text(caseItem.title)
                .font(.dsHeadlineSmall)
                .lineLimit(3)
                .lineSpacing(2)

            // Вознаграждение
            Text(Formatters.reward(caseItem.reward))
                .font(.system(size: 20, weight: .bold))
                .foregroundColor(.primary)

            // Компания
            Text(caseItem.companyName)
                .font(.dsBodyMedium)
                .foregroundColor(.onSurfaceVariant)

            // Дедлайн
            HStack(spacing: 6) {
                Image(systemName: "calendar")
                    .font(.system(size: 12))
                    .foregroundColor(.onSurfaceVariant)
                Text("до \(Formatters.deadline(caseItem.deadline))")
                    .font(.dsCaption)
                    .foregroundColor(.onSurfaceVariant)
            }

            PrimaryButton("Откликнуться", action: onTap)
                .frame(height: 44)
        }
        .padding(16)
        .background(Color.surfaceCard)
        .cornerRadius(16)
        .shadow(color: .black.opacity(0.06), radius: 4, x: 0, y: 2)
        .onTapGesture {
            onTap()
        }
    }
}
