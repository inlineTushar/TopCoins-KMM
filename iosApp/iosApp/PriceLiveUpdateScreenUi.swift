import SwiftUI
import Shared

struct PriceLiveUpdateScreenUi : View {
    @ObservedObject
    var vm: SwiftPriceLiveUpdateViewModel = SwiftPriceLiveUpdateViewModel()

    @ViewBuilder
    var body: some View {
        VStack {
            switch vm.uiState {
            case is PriceLiveUpdateUiState.Loading:
                ProgressBarUi()
            case let state as PriceLiveUpdateUiState.Tick:
                PriceView(
                    symbol: state.symbol,
                    isHiked: state.isHiked,
                    priceWithCurrency: state.price
                )
            default:
                EmptyView()
            }
        }
    }
}

struct PriceView: View {
    let symbol: String
    let isHiked: KotlinBoolean?
    let priceWithCurrency: String

    @State private var gradientColors: [Color] = [
        Color(
            red: Double.random(in: 0...1),
            green: Double.random(in: 0...1),
            blue: Double.random(in: 0...1)
        ),
        Color(
            red: Double.random(in: 0...1),
            green: Double.random(in: 0...1),
            blue: Double.random(in: 0...1)
        )
    ]

    var body: some View {
        VStack {
            Spacer()

            VStack(alignment: .leading) {
                Text(symbol)
                    .font(.system(size: 28, weight: .thin, design: .monospaced))
                    .frame(maxWidth: .infinity, alignment: .center)

                HStack(alignment: .center) {
                    GradientText(
                        text: priceWithCurrency,
                        colors: gradientColors
                    )

                    VStack(spacing: 0) {
                        PriceHikeIndicator(
                            visible: isHiked?.boolValue == true,
                            key: priceWithCurrency
                        )
                        PriceDownIndicator(
                            visible: isHiked?.boolValue == false,
                            key: priceWithCurrency
                        )
                    }
                    .padding(.leading, 4)
                }
                .frame(maxWidth: .infinity, alignment: .center)
            }

            Spacer()
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

struct GradientText: View {
    let text: String
    let colors: [Color]

    var body: some View {
        Text(text)
            .font(.system(size: 48, weight: .bold, design: .monospaced))
            .overlay(
                LinearGradient(
                    colors: colors,
                    startPoint: .leading,
                    endPoint: .trailing
                )
            )
            .mask(
                Text(text)
                    .font(.system(size: 48, weight: .bold, design: .monospaced))
            )
    }
}

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
