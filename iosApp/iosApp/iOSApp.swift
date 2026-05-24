import SwiftUI
import Shared
import FirebaseCore

@main
struct iOSApp: App {

    init() {
        FirebaseApp.configure()
        initKoin()
    }

    var body: some Scene {
        WindowGroup {
            RootView()
        }
    }

    private func initKoin() {
        let config = Configuration(
            isHttpLoggingEnabled: true,
            isDebug: true
        )
        CommonKmp.shared.doInitKoin(configuration: config) { _ in }
    }
}
