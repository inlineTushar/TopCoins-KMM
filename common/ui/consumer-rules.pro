# Consumer ProGuard rules for :common:ui module
# These rules are automatically applied to any module that depends on this one

# ==============================
# Jetpack Compose UI Components
# ==============================
-keep class androidx.compose.** { *; }
-keep class androidx.compose.ui.** { *; }
-keep class androidx.compose.material3.** { *; }
-keep class androidx.compose.foundation.** { *; }
-keep class androidx.compose.animation.** { *; }

# Keep all composable functions
-keep @androidx.compose.runtime.Composable class * { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}

# ==============================
# Navigation Compose
# ==============================
-keep class androidx.navigation.compose.** { *; }

# ==============================
# UI Theme and Components
# ==============================
# Keep custom theme classes
-keep class com.tushar.ui.theme.** { *; }

# Keep custom UI components
-keep class com.tushar.ui.component.** { *; }

# ==============================
# Compose Tooling (Development)
# ==============================
# Keep tooling classes for preview and debugging
-keep class androidx.compose.ui.tooling.** { *; }
-keep class androidx.compose.ui.tooling.preview.** { *; }

# ==============================
# Debugging Support
# ==============================
# Keep parameter names for better debugging
-keepparameternames

# Keep source file and line numbers for crash reports
-keepattributes SourceFile,LineNumberTable

# Rename source file attribute to hide original source file name
-renamesourcefileattribute SourceFile