# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

-keepclassmembers class * {
    public <init>(...);
}

# Kotlinx Serialization
-keep class kotlinx.serialization.** { *; }
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Coroutines
-keep class kotlinx.coroutines.** { *; }
