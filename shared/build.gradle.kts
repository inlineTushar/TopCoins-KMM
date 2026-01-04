plugins {
    alias(libs.plugins.convention.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.touchlab.skie)
}

android {
    namespace = "com.tushar.shared"
}

compose {
    resources {
        packageOfResClass = "com.tushar.shared.resources"
        generateResClass = always
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                // Compose Multiplatform - Required for ComposeUIViewController
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                // Common modules - shared across all platforms
                api(project(":common:core"))
                api(project(":common:ui"))
                api(project(":common:data"))
                api(project(":common:domain"))

                // Feature modules
                api(project(":feature:coinlist"))
                api(project(":feature:priceupdate"))
                implementation(libs.touchlab.skie.annotations)
                api(libs.compose.multiplatform.navigation)
            }
        }
    }

    // Configure iOS framework export - targets are already created by the plugin
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries.withType<org.jetbrains.kotlin.gradle.plugin.mpp.Framework> {
            // Export all dependencies to make them available to iOS
            export(project(":feature:coinlist"))
            export(project(":feature:priceupdate"))
            export(project(":common:core"))
            export(project(":common:ui"))
            export(project(":common:data"))
            export(project(":common:domain"))
        }
    }

    task("testClasses")
}

android {
    dependencies {
        implementation(libs.androidx.core.ktx)
        implementation(libs.koin.android)
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
