import Foundation
import Shared
import Combine

class SwiftPriceLiveUpdateViewModel: ObservableObject {
    
    var viewModel: PriceLiveUpdateViewModel
    private var uiStateTask: Task<Void, Never>?

    @Published
    private(set) var uiState: PriceLiveUpdateUiState = PriceLiveUpdateUiState.Loading.shared

    init() {
        self.viewModel = KotlinDependencies.shared.getPriceLiveUpdateViewModel()
        uiStateTask = Task {
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
        uiStateTask?.cancel()
        uiStateTask = nil
    }

    deinit {
        deactivate()
    }
}
