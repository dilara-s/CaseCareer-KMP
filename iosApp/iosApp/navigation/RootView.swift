import SwiftUI

struct RootView: View {
    // multiplatform-settings хранит токен в UserDefaults под ключом "access_token"
    @State private var isLoggedIn: Bool = UserDefaults.standard.string(forKey: "access_token") != nil

    var body: some View {
        Group {
            if isLoggedIn {
                MainTabView(onLogout: {
                    isLoggedIn = false
                })
            } else {
                AuthView(onNavigateToMain: {
                    isLoggedIn = true
                })
            }
        }
        .animation(.easeInOut(duration: 0.25), value: isLoggedIn)
    }
}
