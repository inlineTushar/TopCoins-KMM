import Foundation
import Shared
import Combine

class SwiftCoinListViewModel: ObservableObject {

    var viewModel: CoinListViewModel
    private var uiStateTask: Task<Void, Never>?
    private var navEventTask: Task<Void, Never>?

    @Published
    private(set) var uiState: CoinsUiState = CoinsUiState.Loading.shared

    @Published
    private(set) var navEvent: NavEvent? = nil

    init() {
        self.viewModel = KotlinDependencies.shared.getCoinListViewModel()

        // Start observing the UI state and nav events
        uiStateTask = Task {
            await activateUiState()
        }
        navEventTask = Task {
            await activateNavEvent()
        }
    }

    private func startCollectorsIfNeeded() {
        if uiStateTask == nil {
            uiStateTask = Task { await activateUiState() }
        }
        if navEventTask == nil {
            navEventTask = Task { await activateNavEvent() }
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

    func deactivate() {
        uiStateTask?.cancel()
        navEventTask?.cancel()
        uiStateTask = nil
        navEventTask = nil
    }

    deinit {
        deactivate()
    }

    func ensureCollectors() {
        startCollectorsIfNeeded()
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
