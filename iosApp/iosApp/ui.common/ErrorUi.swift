import SwiftUI

struct ErrorUi: View {
    let errorText: String
    let onRetry: () -> Void
    let tag: String

    init(errorText: String, onRetry: @escaping () -> Void, tag: String = String(localized: "common_error_occurred")) {
        self.errorText = errorText
        self.onRetry = onRetry
        self.tag = tag
    }

    var body: some View {
        ZStack {
            Color.clear
            VStack {
                Text(errorText)
                    .multilineTextAlignment(.center)
                    .padding(.bottom, 16)
                Button(action: onRetry) {
                    Text("common_retry")
                }
            }
            .padding(16)
            .accessibilityIdentifier(tag)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .contentShape(Rectangle())
        .accessibilityIdentifier(tag)
    }
}
