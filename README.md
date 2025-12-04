## Objective

Develop an Android application that displays a list of the best/worst performing crypto coins in the
last 24 hours.

## Requirements

* Display a list of the top 10 crypto coins based on price change percentage over the last 24 hours
* Provide a way to switch between the 10 best and 10 worst performing crypto coins
* Each list item should contain: Name, Symbol, Change % (format: xx.xx%), Price in Euro
* Provide an option to refresh the list using fresh data
* Use Jetpack Compose for the UI implementation

## App Name: Top Coins

---

## Preview

![Preview](preview/preview1.png)

---

## Architecture Overview

The application follows **Clean Architecture** principles with clear separation of concerns across
multiple modules.

### Architecture Layers

- **Presentation Layer**: Jetpack Compose UI with `ViewModel` for state management
- **Domain Layer**: Contains use cases and business logic, independent of frameworks
- **Data Layer**: Repository pattern with Retrofit for API communication, handles data fetching and
  caching

**Key Patterns**:

- **MVVM** for UI architecture (ViewModel + StateFlow)
- **Unidirectional Data Flow** for predictable state management
- **Dependency Injection** via Koin for loose coupling and simplicity
- **Result-based error handling** for robust failure scenarios


### Clean Architecture: Modules, Layers & Data Flow

This diagram shows the complete architecture combining Clean Architecture layers, module
relationships, dependency rules, and data flow:

```mermaid
graph TB
    subgraph "📱 Presentation Layer - feature:coinlist"
        UI["🖼️ CoinListScreen<br/>(Composable)"]
        VM["🎭 CoinListViewModel<br/>Koin ViewModel<br/>StateFlow&lt;CoinsUiState&gt;"]
        STATE["📊 CoinsUiState<br/>(Sealed Class)<br/>Loading | Content | Error"]
        MODEL["📦 CoinUIModel<br/>"]
    end

    subgraph "🎯 Domain Layer - common:domain"
        UC["⚙️ GetCoinUseCase<br/>Koin Single<br/>Business Logic"]
        REPO_INT["📋 CoinRepository<br/>⭐ INTERFACE<br/>(Dependency Inversion)"]
        DOMAIN_MODEL["🎲 Domain Models<br/>CoinsDomainModel<br/>CoinDomainModel"]
        ERROR["❌ DomainError<br/>(Sealed Class)<br/>NoConnectivity | Timeout"]
    end

    subgraph "💾 Data Layer - common:data"
        REPO["🗄️ CoinRepositoryImpl<br/>Koin Single<br/>+ In-Memory Cache"]
        KOIN_MODULE["🔧 dataModule<br/>Koin Module DSL<br/>Provides Dependencies"]
        API["🌐 CoinApiService<br/>(Retrofit Interface)"]
        INTERCEPTOR["🔐 TokenInterceptor<br/>(Auth Header)"]
        CRYPTO["🔒 SecureKeyProvider<br/>(AES-256-GCM)"]
        REPO_MODEL["📝 Repo Models<br/>CoinsRepoModel"]
        API_MODEL["📡 API Models<br/>CoinsApiResponse"]
    end

    subgraph "🌍 External"
        NETWORK["☁️ CoinCap API<br/>REST Endpoint"]
    end

    %% Data Flow - Solid Lines (source code dependencies)
    UI -->|"1️⃣ User Action<br/>(Pull Refresh)"| VM
    VM -->|"2️⃣ collect()<br/>StateFlow"| STATE
    STATE -->|contains| MODEL
    VM -->|"3️⃣ Koin inject<br/>calls method "| UC
    UC -->|" 4️⃣ Koin inject<br/>uses interface "| REPO_INT
    UC -->|" 5️⃣ returns "| DOMAIN_MODEL
    UC -->|" 6️⃣ throws on error "| ERROR
%% Data Layer Implementation (Clean Architecture boundary)
    REPO_INT -.->|" 7️⃣ implemented by<br/>(via bind) "| REPO
    KOIN_MODULE -.->|"🔧 Provides at runtime<br/>(DI wiring)"| REPO
    
    %% Data Flow Inside Data Layer
    REPO -->|"8️⃣ fetch()<br/>with retry"| API
    API -->|"9️⃣ adds header"| INTERCEPTOR
    INTERCEPTOR -->|"🔟 gets token"| CRYPTO
    API -->|"1️⃣1️⃣ HTTP GET"| NETWORK
    NETWORK -->|"1️⃣2️⃣ JSON Response"| API
    API -->|"1️⃣3️⃣ deserialize"| API_MODEL
    API_MODEL -->|"1️⃣4️⃣ map to"| REPO_MODEL
    REPO_MODEL -->|"1️⃣5️⃣ map to"| DOMAIN_MODEL
    DOMAIN_MODEL -->|"1️⃣6️⃣ back to"| UC
    UC -->|"1️⃣7️⃣ map to"| MODEL
    MODEL -->|"1️⃣8️⃣ emit"| STATE
    STATE -->|"1️⃣9️⃣ render"| UI

    %% Koin DI Dependency (Runtime DI)
    VM -.->|" 💉 Koin Runtime DI<br/>(No code generation) "| KOIN_MODULE
%% Styling
    classDef presentation fill: #e3f2fd, stroke: #1976d2, stroke-width: 3px, color: #000
    classDef domain fill: #fff9c4, stroke: #f57f17, stroke-width: 3px, color: #000
    classDef data fill: #f3e5f5, stroke: #7b1fa2, stroke-width: 3px, color: #000
    classDef koin fill: #c8e6c9, stroke: #388e3c, stroke-width: 4px, color: #000
    classDef external fill: #ffebee, stroke: #c62828, stroke-width: 3px, color: #000
    class UI, VM, STATE, MODEL presentation
    class UC, REPO_INT, DOMAIN_MODEL, ERROR domain
    class REPO, API, INTERCEPTOR, CRYPTO, REPO_MODEL, API_MODEL data
    class KOIN_MODULE koin
    class NETWORK external
```

## Testing

The application includes comprehensive test coverage:

- **Unit Tests**: Test business logic, ViewModels, use cases, and data layer components in isolation
- **UI Tests**: Compose UI tests to verify user interactions and screen behavior

Run tests using:

```bash
# Run all unit tests
./gradlew test

# Run all instrumentation (UI) tests
./gradlew connectedAndroidTest
```

---

## Modularization & Multi-Module Structure

The project uses a **feature-based modularization** strategy:

```
app/                    # Application module (composition root)
├── feature/
│   └── coinlist/      # Feature module (self-contained)
└── common/
    ├── data/          # Data layer (repositories, network)
    ├── domain/        # Domain layer (use cases, models)
    ├── ui/            # Shared UI components & theme
    └── navigation/    # Navigation definitions
```

### Custom Gradle Plugin Benefits

The project uses **custom convention plugins** (`build-logic/`) to standardize build configuration:

- **Consistency**: Unified build configuration across all modules
- **Reusability**: Common plugins like `library-feature`, `library-composeview`, `library-koin`
- **Maintainability**: Single source of truth for dependency versions and build settings
- **Encryption Plugin**: Custom plugin for secure API key encryption/decryption at build/runtime

**Example**: The `library-feature` plugin automatically configures Koin, Compose, testing, and
common dependencies for feature modules.

---

## Build Instructions

### 1. API Key Configuration

The project uses **encrypted API keys** for security. Keys are encrypted at build time and decrypted
at runtime.

**Steps**:

1. Navigate to the `secret/` directory
2. Open `key.properties` (or create it from `key.properties.template`)
3. Add your CoinCap TOKEN key:
   ```properties
   COIN_AUTH_KEY=your_coincap_token_key_here
   ```
4. The encryption plugin will automatically encrypt this file during build and place it in
   `common/data/src/main/res/raw/`

### 2. Keystore Configuration

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

### 3. Build the Project

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease
```
