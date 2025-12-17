import SwiftUI

struct HeaderUi<Top: View, Bottom: View>: View {
    let top: Top
    let bottom: Bottom

    init(@ViewBuilder top: () -> Top, @ViewBuilder bottom: () -> Bottom = {
        EmptyView()
    }) {
        self.top = top()
        self.bottom = bottom()
    }

    var body: some View {
        VStack(alignment: .center, spacing: 0) {
            top
            bottom
        }
        .frame(maxWidth: .infinity)
        .background(Color.accentColor)
        .padding(12)
    }
}
