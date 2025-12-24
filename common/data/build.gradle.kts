plugins {
    alias(libs.plugins.convention.multiplatform)
    alias(libs.plugins.convention.encryption)  // Keep for Android
    alias(libs.plugins.buildkonfig)       // For iOS build-time configuration
    alias(libs.plugins.convention.library.koin) // Koin DI (KMM-aware)
    alias(libs.plugins.convention.library.test) // Test dependencies (KMM-aware)
}

android {
    namespace = "com.tushar.common.data"
}

buildkonfig {
    packageName = "com.tushar.common.data"

    // Default config (used for all platforms unless overridden)
    defaultConfigs {
        // Read COIN_AUTH_KEY from secret/key.properties at build time
        val keyPropsFile = rootProject.file("secret/key.properties")
        val coinAuthKey = if (keyPropsFile.exists()) {
            // Read file line by line to extract COIN_AUTH_KEY
            keyPropsFile.readLines()
                .firstOrNull { it.startsWith("COIN_AUTH_KEY=") }
                ?.substringAfter("COIN_AUTH_KEY=")
                ?.trim()
                ?: ""
        } else {
            // Fallback if secret file doesn't exist
            ""
        }

        buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "COIN_AUTH_KEY", coinAuthKey)
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":common:core"))
            // Domain layer
            implementation(project(":common:domain"))

            // Ktor - Networking (multiplatform)
            api(libs.ktor.client.core)
            api(libs.ktor.client.content.negotiation)
            api(libs.ktor.serialization.kotlinx.json)
            api(libs.ktor.client.logging)
            api(libs.ktor.client.auth)
            api(libs.ktor.client.websocket)

            // Serialization
            api(libs.kotlinx.serialization.json)

            // Note: Koin dependencies added by LibraryKoinConventionPlugin
        }

        androidMain.dependencies {
            // Android-specific Ktor engine
            implementation(libs.ktor.client.okhttp)

            // Note: koin-android added by LibraryKoinConventionPlugin
        }

        iosMain.dependencies {
            // iOS-specific Ktor engine
            implementation(libs.ktor.client.darwin)
        }

        commonTest.dependencies {
            implementation(libs.ktor.client.mock)

            // Note: All standard test dependencies (kotlin.test, mockk, assertk, etc.) 
            //       added by LibraryTestConventionPlugin
            // Note: koin-test added by LibraryKoinConventionPlugin
        }
    }
}
