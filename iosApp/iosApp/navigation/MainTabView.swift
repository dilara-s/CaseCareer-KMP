import SwiftUI

struct MainTabView: View {
    let onLogout: () -> Void

    var body: some View {
        TabView {
            // Лента кейсов (FeedView содержит свой NavigationStack)
            FeedView()
                .tabItem {
                    Label("Главная", systemImage: "square.grid.2x2")
                }

            // Мои отклики
            NavigationStack {
                MyCasesView()
            }
            .tabItem {
                Label("Мои кейсы", systemImage: "heart")
            }

            // Профиль
            NavigationStack {
                ProfileView(onLogout: onLogout)
            }
            .tabItem {
                Label("Аккаунт", systemImage: "person")
            }
        }
        .tint(Color.brandPrimary)
    }
}
