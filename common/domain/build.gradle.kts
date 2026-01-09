plugins {
    alias(libs.plugins.convention.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.convention.library.koin) // Koin DI (KMM-aware)
    alias(libs.plugins.convention.library.test) // Test dependencies (KMM-aware)
}

kotlin {
    androidLibrary {
        namespace = "com.tushar.common.domain"
    }
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
