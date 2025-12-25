import java.util.Properties

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

    defaultConfigs {
        val keyPropsFile = rootProject.file("secret/key.properties")
        if (keyPropsFile.exists()) {
            val properties = Properties()
            keyPropsFile.inputStream().use { properties.load(it) }
            properties.forEach { (key, value) ->
                val cleanValue = value.toString().removeSurrounding("\"")
                buildConfigField(
                    com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
                    key.toString(),
                    cleanValue
                )
            }
        }
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
