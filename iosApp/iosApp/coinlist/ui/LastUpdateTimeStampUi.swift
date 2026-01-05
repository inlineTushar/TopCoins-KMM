import SwiftUI

struct LastUpdateTimeStampUi: View {
    let updatedAtFormatted: String
    var body: some View {
        Text("coin_list_updated_at \(updatedAtFormatted)")
            .foregroundColor(Color.accentColor)
            .font(.system(size: 14))
            .frame(maxWidth: .infinity, alignment: .trailing)
    }
}
