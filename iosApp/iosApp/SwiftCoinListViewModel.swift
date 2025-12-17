import Foundation
import shared
import Combine

class SwiftCoinListViewModel: ObservableObject {
    
    var viewModelStoreOwner = SharedViewModelStoreOwner<CoinListViewModel>()
    var viewModel: CoinListViewModel
    
    @Published
    private(set) var uiState: CoinsUiState = CoinsUiState.Loading.shared

    init() {
        let viewModel = viewModelStoreOwner.instance
        self.viewModel = viewModel
        
        // Start observing the UI state
        Task {
            await activate()
        }
    }
    
    @MainActor
    func activate() async {
        for await uiState in viewModel.uiState {
            self.uiState = uiState
        }
    }

    func deactivate() {
        viewModelStoreOwner.clearViewModel()
    }
    
    func onSort(_ type: SortType) {
        viewModel.onSort(type: type)
    }
    
    func onRetry() {
        viewModel.onRetry()
    }
}

