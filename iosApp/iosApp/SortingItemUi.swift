import SwiftUI
import shared

struct SortingItemUi: View {
    let currentSortType: SortType
    let onClickSort: (SortType) -> Void
    private let sortTypes: [SortType] = [.bestPerform, .worstPerform]
    
    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 12) {
                ForEach(sortTypes, id: \.self) { type in
                    sortButton(for: type)
                }
            }
            .padding(.vertical, 4)
            .padding(.horizontal, 8)
        }
    }
    
    @ViewBuilder
    private func sortButton(for type: SortType) -> some View {
        let isSelected = type == currentSortType
        let textColor = isSelected ? Color.white : Color.accentColor
        let backgroundColor = isSelected ? Color.accentColor : Color(UIColor.secondarySystemBackground)
        
        Button(action: {
            onClickSort(type)
        }) {
            Text(sortTypeTitle(for: type))
                .foregroundColor(textColor)
                .fontWeight(isSelected ? .bold : .regular)
                .padding(.horizontal, 18)
                .padding(.vertical, 8)
                .background(backgroundColor)
                .cornerRadius(10)
        }
    }
    
    // Helper to map the Kotlin sortType resource to a presentable string. Adjust as needed for localization.
    private func sortTypeTitle(for type: SortType) -> String {
        switch type {
        case .bestPerform: return "Best"
        case .worstPerform: return "Worst"
        }
    }
}
