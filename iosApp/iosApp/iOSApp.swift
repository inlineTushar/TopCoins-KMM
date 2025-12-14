import SwiftUI
import shared

@main
struct iOSApp: App {
    init() {
        KoinKt.startKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
