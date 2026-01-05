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
                    .accessibilityLabel("Coin name: \(coinName)")
                Text(coinSymbol)
                    .font(.system(size: 14, weight: .medium, design: .monospaced))
                    .foregroundColor(Color.secondary)
                    .padding(.top, 4)
                    .accessibilityLabel("Symbol: \(coinSymbol)")
            }
            .padding(.leading, 8)
            Spacer()
            VStack(alignment: .trailing, spacing: 8) {
                Text(coinChange)
                    .font(.system(size: 16, weight: .light))
                    .foregroundColor(Color.primary)
                    .accessibilityLabel("Change in last 24 hours: \(coinChange)")
                Text(coinPrice)
                    .font(.system(size: 16, weight: .bold, design: .default))
                    .foregroundColor(Color.accentColor)
                    .accessibilityLabel("Price: \(coinPrice)")
            }
        }
        .padding(8)
        .frame(maxWidth: .infinity)
        .accessibilityLabel("Coin item: \(coinName) (\(coinSymbol)), price \(coinPrice), change in last 24 hours \(coinChange)")
    }
}
