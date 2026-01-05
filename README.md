# Top Coins — Kotlin Multiplatform (KMM)

**Top Coins** is a cross-platform (Android & iOS) crypto analytics app, refactored to KMM from an
original Android-only architecture ([switch to Android-only](https://github.com/inlineTushar/TopCoins-KMM/tree/android-master)).

---

## Preview

| Android                                         | iOS                                     |
|-------------------------------------------------|-----------------------------------------|
|  <video src="https://github.com/user-attachments/assets/aad0c12e-702c-4f08-94b7-7c96d22ba5fc"></video>| <video src="https://github.com/user-attachments/assets/89ae6115-4848-497f-b4fb-786c0155c1c9"></video> |

---

## Modularization Structure (KMM)

```text
app/                        # Android app entry point
iosApp/                     # iOS app (native SwiftUI)
  └── coinlist/             # Coin list feature (ui/, vm/)
  └── priceliveupdate/      # Price live update feature (ui/, vm/)
  └── composeui/            # Compose Multiplatform hosting
  └── ui.common/            # Shared SwiftUI components
  └── util/                 # iOS utilities (Bundle, SharedViewModelStoreOwner)
  └── resources/            # Localization (en.lproj)
shared/                     # KMM umbrella module (exports cross-platform code)
common/
  └── core/                 # Core types, utilities, extensions
  └── data/                 # Repositories, Ktor networking, WebSocket
  └── domain/               # Use cases, domain models
  └── ui/                   # Compose Multiplatform UI theme/components
  └── navigation/           # Cross-platform navigation (JetBrains/AndroidX)
feature/
  └── coinlist/             # Coin list feature module
  └── priceupdate/          # Live price update feature module
build-logic/
  └── convention/           # Custom Gradle convention plugins
  └── security/             # API key encryption plugin
```

- The `shared` umbrella module exports all KMM modules for iOS consumption.
- Platform launchers (`app`, `iosApp`) host the shared code with platform-specific UI.
- `iosApp` contains native SwiftUI views that consume shared ViewModels via SKIE bridge.

---

## KMM Architecture & Principles

- **MVVM** with multiplatform ViewModel (StateFlow)
- **Clean Architecture**: distinct domain, data, UI modules
- **DI:** Koin multiplatform
- **Modular**: Feature modules, core/domain/data split
- **Tests**: Unit tests in all major modules (TODO:// Test are mostly android test,
  KMM commonTest will be added soon)

**iOS UI Consumption Strategies:**

This project demonstrates two approaches for consuming KMM shared code on iOS:

| Strategy | Shared Layer | iOS UI | Example |
|----------|--------------|--------|---------|
| **Compose UI** | End-to-end (Data → Domain → ViewModel → UI) | Compose Multiplatform hosted in SwiftUI | Feature screens |
| **ViewModel Consumption** | Up to ViewModel (Data → Domain → ViewModel) | Native SwiftUI | CoinList, PriceLiveUpdate |

**Key Integration Details:**

- **SKIE Bridge**: When using native SwiftUI, [SKIE](https://skie.touchlab.co/) provides seamless Kotlin-to-Swift interoperability (Flows as AsyncSequence, sealed classes as Swift enums, etc.)
- **Cross-platform Navigation**: JetBrains/AndroidX Compose Navigation ensures navigation logic remains consistent across platforms
- **Shared ViewState**: ViewModels expose the same `UiState` sealed classes to both Android Compose and iOS SwiftUI, ensuring consistent behavior

---

## Clean Code Practices

| Principle                  | How It's Done                                                      |
|----------------------------|--------------------------------------------------------------------|
| **Separation of Concerns** | Layered Clean Architecture, per-domain modules                     |
| **Testability**            | Unit & UI Tests, DI with Koin                                      |
| **Modularity**             | Feature & domain modules, Compose resource encapsulation           |
| **Build Hygiene**          | Custom Gradle convention plugins                                   |
| **KMM Best-Practices**     | Platform-specific logic in respective sourceSets, umbrella sharing |

---

## API Key Setup

This app requires API keys from [CoinCap](https://coincap.io/) and [Twelve Data](https://twelvedata.com/).

1. Register at [CoinCap API portal](https://coincap.io/api) and [Twelve Data](https://twelvedata.com/) to get your API tokens.
2. Open (or create) `secret/key.properties` in your project (the file is ignored by git).
3. Add your keys in the following format:

   ```properties
   KEY_COIN_AUTH=<your CoinCap API token here>
   KEY_12DATA=<your Twelve Data API token here>
   ```
4. Never commit this file. It is already gitignored for safety!

---

### Keystore Configuration

The app requires signing configurations for both debug and release builds.

**Debug Build**:

- Debug keystore is included: `keystore/debug.keystore.jks`
- No additional configuration needed

**Release Build**:
1. Release keystore is included: `keystore/release.keystore.jks`
2. Obtain the release signing credentials from the developer
3. Add credentials to your **global gradle.properties** (`~/.gradle/gradle.properties`):
   ```properties
   COIN_RELEASE_KEY_ALIAS=your_key_alias
   COIN_RELEASE_KEY_PASSWORD=your_key_password
   COIN_RELEASE_STORE_PASSWORD=your_store_password
   ```

### Build the Project

**Android:**

```bash
# Debug build
./gradlew :app:assembleDebug

# Release build
./gradlew :app:assembleRelease
```

**iOS:**

Open `iosApp/iosApp.xcodeproj` in Xcode and build/run from there, or use the command line:

```bash
# Build iOS app (Debug)
xcodebuild -project iosApp/iosApp.xcodeproj -scheme iosApp -configuration Debug -sdk iphonesimulator build
```

## Switch to Android-only

This is the `KMM` multiplatform branch. For the pure Android
app, [checkout here](https://github.com/inlineTushar/TopCoins-KMM/tree/android-master).