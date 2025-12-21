import SwiftUI

struct LastUpdateTimeStampUi: View {
    let updatedAtFormatted: String
    var body: some View {
        Text("Updated at: \(updatedAtFormatted)")
            .foregroundColor(Color.accentColor)
            .font(.system(size: 14))
            .frame(maxWidth: .infinity, alignment: .trailing)
    }
}
