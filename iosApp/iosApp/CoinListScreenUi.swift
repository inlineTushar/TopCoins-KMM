import SwiftUI
import shared

struct RefreshableModifier: ViewModifier {
    let action: () -> Void

    func body(content: Content) -> some View {
        if #available(iOS 15.0, *) {
            content.refreshable { action() }
        } else {
            content
        }
    }
}

struct CoinListScreenUi: View {
    
    @ObservedObject
    var vm: SwiftCoinListViewModel = SwiftCoinListViewModel()
    
    @ViewBuilder
    var body: some View {
        VStack {
            switch vm.uiState {
            case is CoinsUiState.Loading:
                ProgressBarUi()
            case let errorState as CoinsUiState.Error:
                ErrorUi(
                    errorText: errorState.error,
                    onRetry: vm.onRetry
                )
            case let contentState as CoinsUiState.Content:
                VStack {
                    HeaderUi {
                        SortingItemUi(
                            currentSortType: contentState.type,
                            onClickSort: vm.onSort
                        )
                    } bottom: {
                        LastUpdateTimeStampUi(
                            updatedAtFormatted: contentState.updatedAt
                        )
                    }
                    List {
                        ForEach(contentState.items, id: \.id) { item in
                            CoinItemUi(
                                coinName: item.name,
                                coinSymbol: item.symbol,
                                coinPrice: item.price,
                                coinChange: item.changePercent24Hr
                            )
                        }
                    }
                }.modifier(RefreshableModifier(action: vm.onReload))
            default:
                EmptyView()
            }
        }
        .navigationTitle(Bundle.main.appName)
        .navigationBarTitleDisplayMode(.inline)
        .background(Color(UIColor.systemBackground))
    }
}

struct CoinListScreenUi_Previews: PreviewProvider {
    static var previews: some View {
        CoinListScreenUi()
    }
}
