plugins {
    alias(libs.plugins.local.library)
    alias(libs.plugins.local.library.test)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.tushar.navigation"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    api(libs.androidx.navigation.compose)
}
