import SwiftUI
import shared

struct CoinListScreenUi: View {
    
    @ObservedObject
    var vm: SwiftCoinListViewModel = SwiftCoinListViewModel()
    
    @ViewBuilder
    var body: some View {
        if #available(iOS 16.0, *) {
            NavigationStack {
                VStack {
                    switch vm.uiState {
                    case is CoinsUiState.Loading:
                        ProgressBarUi()
                    case let errorState as CoinsUiState.Error:
                        ErrorUi(
                            errorText: errorState.errorString,
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
                        }.refreshable { vm.onReload() }
                    default:
                        EmptyView()
                    }
                }
                .navigationTitle(Bundle.main.appName)
                .navigationBarTitleDisplayMode(.inline)
                .background(Color(UIColor.systemBackground))
            }
        } else {
            // TODO:// Fallback on earlier versions
        }
    }
}

struct CoinListScreenUi_Previews: PreviewProvider {
    static var previews: some View {
        CoinListScreenUi()
    }
}
