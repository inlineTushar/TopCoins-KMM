import SwiftUI
import shared

@main
struct iOSApp: App {
    init() {
        KoinKt.startKoin()
    }

    var body: some Scene {
        WindowGroup {
            NavigationView {
                VStack(spacing: 20) {
                    NavigationLink("SwiftUI") {
                        CoinListScreenUi()
                            .navigationBarBackButtonHidden(true)
                    }
                    NavigationLink("ComposeUI") {
                        ContentView()
                            .navigationBarBackButtonHidden(true)
                    }
                }
            }
            .navigationViewStyle(.stack)
        }
    }
}
