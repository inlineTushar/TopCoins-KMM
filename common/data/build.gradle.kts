plugins {
    alias(libs.plugins.local.library)
    alias(libs.plugins.local.library.koin)
    alias(libs.plugins.local.library.test)
    alias(libs.plugins.local.encryption)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.tushar.data"
}

dependencies {
    // Data layer depends on domain layer
    implementation(project(":common:domain"))

    api(libs.retrofit)
    api(libs.retrofit.kotlinx.serialization)
    api(libs.kotlinx.serialization.json)
    api(libs.okhttp)
    api(libs.okhttp.logging)
}