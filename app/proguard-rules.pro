# ProGuard rules for :app module

# ==============================
# Application Class
# ==============================
# Keep the Application class - required for Hilt
-keep class com.tushar.topcoins.ChallengeApplication { *; }
-keep @dagger.hilt.android.HiltAndroidApp class * { *; }

# ==============================
# Activities
# ==============================
# Keep all activities - required for Android system
-keep class com.tushar.topcoins.MainActivity { *; }
-keep @dagger.hilt.android.AndroidEntryPoint class * extends androidx.activity.ComponentActivity { *; }

# Keep activity lifecycle methods
-keepclassmembers class * extends androidx.activity.ComponentActivity {
    public void onCreate(android.os.Bundle);
    public void onStart();
    public void onResume();
    public void onPause();
    public void onStop();
    public void onDestroy();
}

# ==============================
# Navigation
# ==============================
# Keep navigation graph and composables
-keep class com.tushar.topcoins.MainNavGraph** { *; }
-keepclassmembers class com.tushar.topcoins.** {
    @androidx.compose.runtime.Composable *;
}

# ==============================
# Hilt (Application Module)
# ==============================
# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ApplicationComponentManager { *; }

# Keep Hilt entry points
-keep @dagger.hilt.InstallIn class * { *; }
-keep @dagger.Module class * { *; }

# Keep Hilt component interfaces
-keep interface dagger.hilt.android.components.** { *; }

# ==============================
# AndroidX Core
# ==============================
# Keep ComponentActivity and related classes
-keep class androidx.activity.ComponentActivity { *; }
-keep class androidx.activity.compose.** { *; }

# Keep edge-to-edge support
-keep class androidx.core.view.WindowCompat { *; }

# ==============================
# Jetpack Compose (App Level)
# ==============================
# Already covered by :common:ui consumer rules, but keep entry points
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.platform.** { *; }

# Keep Compose theme
-keep class com.tushar.ui.theme.** { *; }

# ==============================
# Permissions
# ==============================
# Keep permission-related classes (INTERNET permission used)
-keep class android.Manifest$permission { *; }

# ==============================
# R8 Optimizations
# ==============================
# Remove logging in release builds (R8 optimization)
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

-assumenosideeffects class timber.log.Timber {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# ==============================
# Debugging Support
# ==============================
# Keep parameter names for better debugging
-keepparameternames

# Keep source file and line numbers for crash reports
-keepattributes SourceFile,LineNumberTable

# Rename source file attribute to hide original source file name
-renamesourcefileattribute SourceFile

# Keep annotations for debugging
-keepattributes *Annotation*

# ==============================
# Reflection Support
# ==============================
# Keep inner classes that might be accessed via reflection
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# ==============================
# Serialization Support
# ==============================
# Keep Signature attribute for generics
-keepattributes Signature

# Keep exceptions for better error reporting
-keepattributes Exceptions
