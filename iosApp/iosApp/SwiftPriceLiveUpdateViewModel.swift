import Foundation
import shared
import Combine

class SwiftPriceLiveUpdateViewModel: ObservableObject {
    
    var viewModelStoreOwner = SharedViewModelStoreOwner<PriceLiveUpdateViewModel>()
    var viewModel: PriceLiveUpdateViewModel

    @Published
    private(set) var uiState: PriceLiveUpdateUiState = PriceLiveUpdateUiState.Loading.shared

    init() {
        let viewModel = viewModelStoreOwner.instance
        self.viewModel = viewModel
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
}
