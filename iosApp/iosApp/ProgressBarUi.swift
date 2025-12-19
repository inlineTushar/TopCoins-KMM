import SwiftUI

struct ProgressBarUi: View {

    var body: some View {
        ProgressView()
            .progressViewStyle(CircularProgressViewStyle())
            .scaleEffect(1.5)
            .frame(maxWidth: .infinity)
    }
}
