import SwiftUI
import shared

struct CoinListScreenUi: View {
    
    @ObservedObject
    var vm: SwiftCoinListViewModel = SwiftCoinListViewModel()
    
    var body: some View {
        VStack {
            if vm.uiState is CoinsUiState.Loading {
                ProgressBarUi()
            } else if let errorState = vm.uiState as? CoinsUiState.Error {
                ErrorUi(
                    errorText: errorState.errorString,
                    onRetry: vm.onRetry
                )
            } else if let contentState = vm.uiState as? CoinsUiState.Content {
                VStack(spacing: 0) {
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
                }
            }
        }
        .navigationTitle("Coins")
        .background(Color(UIColor.systemBackground))
    }
}

struct CoinListScreenUi_Previews: PreviewProvider {
    static var previews: some View {
        CoinListScreenUi()
    }
}

