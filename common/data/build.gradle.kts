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

    // Ktor - Networking
    api(libs.ktor.client.core)
    api(libs.ktor.client.android)
    api(libs.ktor.client.content.negotiation)
    api(libs.ktor.serialization.kotlinx.json)
    api(libs.ktor.client.logging)
    api(libs.ktor.client.auth)

    // Serialization
    api(libs.kotlinx.serialization.json)

    // Testing
    testImplementation(libs.ktor.client.mock)
}