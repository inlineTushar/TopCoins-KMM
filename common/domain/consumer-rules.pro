# Consumer ProGuard rules for :common:domain module
# These rules are automatically applied to any module that depends on this one

# ==============================
# Kotlinx Serialization
# ==============================
-keep class kotlinx.serialization.** { *; }
-keepattributes *Annotation*, InnerClasses

# Keep @Serializable classes - CRITICAL for your domain models
-keep @kotlinx.serialization.Serializable class * {
    <fields>;
    <init>(...);
}

# Keep serialization descriptors
-keep class * implements kotlinx.serialization.KSerializer {
    <fields>;
    <methods>;
}

# Keep companion objects for serializable classes
-keepclassmembers @kotlinx.serialization.Serializable class * {
    public static **$Companion Companion;
}

# Keep serialization companion objects
-keepclassmembers class * {
    **$serializer $serializer(...);
}

# Don't warn about kotlinx.serialization compiler plugin generated code
-dontnote kotlinx.serialization.AnnotationsKt
-dontwarn kotlinx.serialization.KSerializer
-dontwarn kotlinx.serialization.Serializable

# ==============================
# Project-specific Domain Models
# ==============================
# Keep all domain models
-keep class com.tushar.domain.model.** { *; }

# Keep all use cases
-keep class com.tushar.domain.*UseCase { *; }
-keep class com.tushar.domain.**.*UseCase { *; }

# Keep domain repositories (interfaces)
-keep interface com.tushar.domain.repository.** { *; }

# Keep domain errors (sealed class hierarchy)
-keep class com.tushar.domain.DomainError { *; }
-keep class com.tushar.domain.DomainError$* { *; }

# ==============================
# Coroutines & Flow
# ==============================
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# Keep Flow classes used in use cases
-keep class kotlinx.coroutines.flow.** { *; }

# ==============================
# Debugging Support
# ==============================
# Keep parameter names for better debugging
-keepparameternames

# Keep source file and line numbers for crash reports
-keepattributes SourceFile,LineNumberTable

# Rename source file attribute to hide original source file name
-renamesourcefileattribute SourceFile
