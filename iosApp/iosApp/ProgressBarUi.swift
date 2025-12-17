import SwiftUI

struct ProgressBarUi: View {
    var progress: Double = 0.5

    var body: some View {
        ProgressView(value: progress)
            .progressViewStyle(LinearProgressViewStyle())
            .frame(maxWidth: .infinity)
    }
}
