//
//  ComposeView.swift
//  iosApp
//
//  Created by Tushar Saha on 17/12/25.
//  Copyright © 2025 Tushar. All rights reserved.
//

import SwiftUI
import Shared


struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        return ViewControllerKt.iOSEntryPointViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // No updates needed
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
