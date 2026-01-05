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
