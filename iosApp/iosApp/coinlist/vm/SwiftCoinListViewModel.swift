import Foundation
import Shared
import Combine

class SwiftCoinListViewModel: ObservableObject {

    var viewModelStoreOwner = SharedViewModelStoreOwner<CoinListViewModel>()
    var viewModel: CoinListViewModel

    @Published
    private(set) var uiState: CoinsUiState = CoinsUiState.Loading.shared

    @Published
    private(set) var navEvent: NavEvent? = nil

    init() {
        let viewModel = viewModelStoreOwner.instance
        self.viewModel = viewModel

        // Start observing the UI state and nav events
        Task {
            await activateUiState()
        }
        Task {
            await activateNavEvent()
        }
    }

    @MainActor
    func activateUiState() async {
        for await uiState in viewModel.uiState {
            self.uiState = uiState
        }
    }

    @MainActor
    func activateNavEvent() async {
        for await event in viewModel.navEvent {
            self.navEvent = event
        }
    }

    func consumeNavEvent() {
        navEvent = nil
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

    func onReload() {
        viewModel.onReload()
    }

    func onOptionPriceLiveClick() {
        viewModel.onOptionPriceLiveClick()
    }
}
