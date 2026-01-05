import SwiftUI
import Shared

struct PriceHikeIndicator: View {
    let visible: Bool
    let key: String

    @State private var opacity: Double = 0

    var body: some View {
        Image(systemName: "arrow.up")
            .foregroundColor(.green)
            .opacity(opacity)
            .onChange(of: key) { _ in
                if visible {
                    opacity = 1
                    withAnimation(.easeOut(duration: 0.5)) {
                        opacity = 0
                    }
                } else {
                    opacity = 0
                }
            }
            .onAppear {
                if visible {
                    opacity = 1
                    withAnimation(.easeOut(duration: 0.5)) {
                        opacity = 0
                    }
                }
            }
    }
}

struct PriceDownIndicator: View {
    let visible: Bool
    let key: String

    @State private var opacity: Double = 0

    var body: some View {
        Image(systemName: "arrow.down")
            .foregroundColor(.red)
            .opacity(opacity)
            .onChange(of: key) { _ in
                if visible {
                    opacity = 1
                    withAnimation(.easeOut(duration: 0.5)) {
                        opacity = 0
                    }
                } else {
                    opacity = 0
                }
            }
            .onAppear {
                if visible {
                    opacity = 1
                    withAnimation(.easeOut(duration: 0.5)) {
                        opacity = 0
                    }
                }
            }
    }
}
