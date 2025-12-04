# Consumer ProGuard rules for :feature:coinlist module
# These rules are automatically applied to any module that depends on this one

# ==============================
# ViewModels
# ==============================
# Keep all ViewModels - required for Koin injection and reflection
-keep class com.tushar.coinlist.*ViewModel { *; }
-keep class com.tushar.coinlist.**.*ViewModel { *; }

# Keep ViewModel constructor parameters
-keepclassmembers class com.tushar.coinlist.*ViewModel {
    <init>(...);
}

# ==============================
# UI Models and State Classes
# ==============================
# Keep all UI models - used in state flow and composables
-keep class com.tushar.coinlist.CoinUIModel { *; }
-keep class com.tushar.coinlist.SortType { *; }
-keep class com.tushar.coinlist.CoinsUiState { *; }
-keep class com.tushar.coinlist.CoinsUiState$** { *; }

# Keep data class properties for UI models
-keepclassmembers class com.tushar.coinlist.CoinUIModel {
    <fields>;
    <init>(...);
}

# Keep sealed class hierarchy for UI states
-keep class com.tushar.coinlist.CoinsUiState$* { *; }
-keepclassmembers class com.tushar.coinlist.CoinsUiState$* {
    <fields>;
    <init>(...);
}

# Keep enum values for SortType
-keepclassmembers enum com.tushar.coinlist.SortType {
    public *;
}

# ==============================
# Formatters
# ==============================
# Keep formatter interfaces and implementations - used for dependency injection
-keep interface com.tushar.coinlist.formatter.** { *; }
-keep class com.tushar.coinlist.formatter.**Formatter { *; }
-keep class com.tushar.coinlist.formatter.Default* { *; }

# Keep formatter constructors for Koin
-keepclassmembers class com.tushar.coinlist.formatter.** {
    <init>(...);
}

# ==============================
# Composable Functions
# ==============================
# Keep all composable functions and their parameters
-keep @androidx.compose.runtime.Composable class com.tushar.coinlist.** { *; }
-keepclassmembers class com.tushar.coinlist.** {
    @androidx.compose.runtime.Composable *;
}

# Keep composable function names for debugging
-keepattributes SourceFile,LineNumberTable

# ==============================
# Koin Modules
# ==============================
# Keep Koin modules and definitions in this feature
-keep class com.tushar.coinlist.di.** { *; }
-keep class org.koin.** { *; }

# Keep classes used in Koin modules
-keepclassmembers class com.tushar.coinlist.** {
    <init>(...);
}

# ==============================
# Kotlinx Immutable Collections
# ==============================
# Keep immutable collections used in UI state
-keep class kotlinx.collections.immutable.** { *; }
-keep interface kotlinx.collections.immutable.** { *; }

# ==============================
# Navigation
# ==============================
# Keep navigation-related classes if any
-keep class com.tushar.coinlist.**Screen** { *; }
-keep class com.tushar.coinlist.**Route** { *; }

# ==============================
# String Resources & Annotations
# ==============================
# Keep string resource references in enums and classes
-keepattributes *Annotation*
-keep class androidx.annotation.StringRes { *; }

# ==============================
# Debugging Support
# ==============================
# Keep parameter names for better debugging
-keepparameternames

# Keep source file and line numbers for crash reports
-keepattributes SourceFile,LineNumberTable

# Rename source file attribute to hide original source file name
-renamesourcefileattribute SourceFile
