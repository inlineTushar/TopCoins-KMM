import SwiftUI

struct CoinItemUi: View {
    let coinName: String
    let coinSymbol: String
    let coinPrice: String
    let coinChange: String

    var body: some View {
        HStack(alignment: .center) {
            VStack(alignment: .leading, spacing: 4) {
                Text(coinName)
                    .font(.system(size: 20, weight: .medium))
                    .foregroundColor(Color.primary)
                    .lineLimit(1)
                    .accessibilityLabel(String(localized: "accessibility_coin_name \(coinName)"))
                Text(coinSymbol)
                    .font(.system(size: 14, weight: .medium, design: .monospaced))
                    .foregroundColor(Color.secondary)
                    .padding(.top, 4)
                    .accessibilityLabel(String(localized: "accessibility_coin_symbol \(coinSymbol)"))
            }
            .padding(.leading, 8)
            Spacer()
            VStack(alignment: .trailing, spacing: 8) {
                Text(coinChange)
                    .font(.system(size: 16, weight: .light))
                    .foregroundColor(Color.primary)
                    .accessibilityLabel(String(localized: "accessibility_coin_change \(coinChange)"))
                Text(coinPrice)
                    .font(.system(size: 16, weight: .bold, design: .default))
                    .foregroundColor(Color.accentColor)
                    .accessibilityLabel(String(localized: "accessibility_coin_price \(coinPrice)"))
            }
        }
        .padding(8)
        .frame(maxWidth: .infinity)
        .accessibilityLabel(String(localized: "accessibility_coin_item \(coinName) \(coinSymbol) \(coinPrice) \(coinChange)"))
    }
}
