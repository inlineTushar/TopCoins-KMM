# Consumer ProGuard rules for :common:data module
# These rules are automatically applied to any module that depends on this one

# ==============================
# OkHttp
# ==============================
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keep class okio.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

# Keep OkHttp interceptors
-keep class * implements okhttp3.Interceptor {
    <init>(...);
    public okhttp3.Response intercept(okhttp3.Interceptor$Chain);
}

# ==============================
# Kotlinx Serialization
# ==============================
-keep class kotlinx.serialization.** { *; }
-keepattributes *Annotation*, InnerClasses

# Keep @Serializable classes - CRITICAL for your data models
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
# Project-specific Data Models
# ==============================
# Keep all repositories and implementations
-keep class com.tushar.data.repository.** { *; }

# Keep all API models and responses
-keep class com.tushar.data.datasource.remote.model.** { *; }
-keep class com.tushar.data.datasource.remote.api.** { *; }

# Keep key provider classes
-keep class com.tushar.data.keyprovider.** { *; }
-keep interface com.tushar.data.keyprovider.** { *; }

# Keep custom serializers
-keep class com.tushar.data.datasource.remote.instrumentation.*Serializer { *; }

# Keep Hilt modules
-keep @dagger.Module class com.tushar.data.** { *; }
-keep @dagger.hilt.InstallIn class com.tushar.data.** { *; }

# ==============================
# Retrofit
# ==============================
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Keep Retrofit service interfaces
-keep interface com.tushar.data.datasource.remote.api.** { *; }

# Keep Retrofit annotations
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes RuntimeInvisibleParameterAnnotations

# Keep Retrofit method annotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# ==============================
# Coroutines
# ==============================
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# ==============================
# Debugging Support
# ==============================
# Keep parameter names for better debugging
-keepparameternames

# Keep source file and line numbers for crash reports
-keepattributes SourceFile,LineNumberTable

# Rename source file attribute to hide original source file name
-renamesourcefileattribute SourceFile