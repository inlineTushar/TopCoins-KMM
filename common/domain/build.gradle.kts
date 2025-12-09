plugins {
    alias(libs.plugins.local.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.local.library.koin) // Koin DI (KMM-aware)
    alias(libs.plugins.local.library.test) // Test dependencies (KMM-aware)
}

android {
    namespace = "com.tushar.common.domain"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":common:core"))
            // Multiplatform datetime
            api(libs.kotlinx.datetime)

            // Coroutines
            api(libs.kotlinx.coroutines.core)
        }

        commonTest.dependencies {
            implementation(libs.ktor.client.mock)

            // Note: All standard test dependencies (kotlin.test, mockk, assertk, etc.) 
            //       added by LibraryTestConventionPlugin
            // Note: koin-test added by LibraryKoinConventionPlugin
        }
    }
}
