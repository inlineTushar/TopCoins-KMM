package com.tushar.plugin

import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin that configures a module for Kotlin Multiplatform.
 *
 * This plugin provides the STRUCTURAL setup for KMM without opinionated dependencies.
 * Each module should declare its own dependencies based on its specific needs.
 *
 * This plugin enables:
 * - Android and iOS targets
 * - Shared source sets (commonMain, androidMain, iosMain, with proper linking)
 * - Proper Android library configuration
 * - iOS framework generation
 * - Minimal test dependencies (kotlin-test only)
 *
 * Usage in module's build.gradle.kts:
 * ```
 * plugins {
 *     alias(libs.plugins.local.multiplatform.conversion)
 * }
 *
 * android {
 *     namespace = "com.yourcompany.yourmodule"
 * }
 *
 * kotlin {
 *     sourceSets {
 *         commonMain.dependencies {
 *             // Declare your module-specific dependencies here
 *             implementation(libs.kotlinx.coroutines.core)
 *             implementation(libs.kotlinx.serialization.json)
 *         }
 *
 *         androidMain.dependencies {
 *             // Android-specific dependencies
 *             implementation(libs.kotlinx.coroutines.android)
 *         }
 *
 *         iosMain.dependencies {
 *             // iOS-specific dependencies
 *         }
 *     }
 * }
 * ```
 */
class MultiplatformConversionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply required plugins
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            // Configure Kotlin Multiplatform
            extensions.configure<KotlinMultiplatformExtension> {
                configureKotlinMultiplatform(target)
            }

            // Configure Android Library
            extensions.configure<LibraryExtension> {
                configureAndroidLibrary()
            }

            // Configure common dependencies
            configureDependencies()
        }
    }
}

/**
 * Configure Kotlin Multiplatform targets and source sets
 */
private fun KotlinMultiplatformExtension.configureKotlinMultiplatform(project: Project) {
    // Android target
    androidTarget {
        compilations.all {
            compilerOptions.configure {
                jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
            }
        }
    }

    // iOS targets
    val iosTargets = listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    // Configure iOS frameworks
    iosTargets.forEach { target ->
        target.binaries.framework {
            baseName = project.name
            isStatic = true
        }
    }

    // Source sets configuration
    sourceSets.apply {
        // Common source set
        getByName("commonMain") {
            dependencies {
                // Common dependencies will be added via configureDependencies
            }
        }

        getByName("commonTest") {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        // Android source set
        getByName("androidMain") {
            dependencies {
                // Android-specific dependencies
            }
        }

        // iOS source set (common for all iOS targets)
        val iosMain = create("iosMain") {
            dependsOn(getByName("commonMain"))
        }

        val iosTest = create("iosTest") {
            dependsOn(getByName("commonTest"))
        }

        // Link iOS target-specific source sets to shared iOS source set
        getByName("iosX64Main").dependsOn(iosMain)
        getByName("iosArm64Main").dependsOn(iosMain)
        getByName("iosSimulatorArm64Main").dependsOn(iosMain)

        getByName("iosX64Test").dependsOn(iosTest)
        getByName("iosArm64Test").dependsOn(iosTest)
        getByName("iosSimulatorArm64Test").dependsOn(iosTest)
    }
}

/**
 * Configure Android library settings for multiplatform
 */
private fun LibraryExtension.configureAndroidLibrary() {
    compileSdk = 36

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Enable build features if needed
    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }
}

/**
 * Configure common dependencies for multiplatform modules
 *
 * Note: This method is intentionally left minimal. Each module should declare
 * its own dependencies based on its specific needs. This keeps the plugin
 * flexible and non-opinionated about what libraries to use.
 */
private fun Project.configureDependencies() {
    dependencies {
        // Only add the absolute minimum - Kotlin test support
        add("commonTestImplementation", kotlin("test"))
    }
}
