package com.tushar.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

internal object IosViewModelStoreOwner : ViewModelStoreOwner {
    override val viewModelStore: ViewModelStore = ViewModelStore()
}

private val cachedViewModels = mutableMapOf<String, ViewModel>()

internal inline fun <reified VM : ViewModel> getOrCreateViewModel(
    key: String,
    factory: () -> VM,
): VM {
    val store = IosViewModelStoreOwner.viewModelStore
    val cached = cachedViewModels[key]
    if (cached != null) return cached as VM

    val existing = store.get(key) as? VM
    if (existing != null) {
        cachedViewModels[key] = existing
        return existing
    }

    val viewModel = factory()
    store.put(key, viewModel)
    cachedViewModels[key] = viewModel
    return viewModel
}

internal fun clearIosViewModelStore() {
    IosViewModelStoreOwner.viewModelStore.clear()
    cachedViewModels.clear()
}

internal fun clearIosViewModel(key: String) {
    val store = IosViewModelStoreOwner.viewModelStore
    cachedViewModels.remove(key)
    store.clear()
    cachedViewModels.forEach { (cachedKey, viewModel) ->
        store.put(cachedKey, viewModel)
    }
}
