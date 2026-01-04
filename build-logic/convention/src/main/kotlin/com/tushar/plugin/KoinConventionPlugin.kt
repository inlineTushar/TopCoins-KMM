package com.tushar.plugin

import com.tushar.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin that adds Koin dependency injection to library modules.
 *
 * Unlike Hilt, Koin doesn't require annotation processing (kapt/ksp) as it uses
 * runtime dependency injection. This plugin adds all necessary Koin dependencies
 * including core, Android support, Compose integration, and testing libraries.
 *
 * This plugin is KMM-aware:
 * - For KMM modules: Adds koin-core to commonMain, koin-android to androidMain
 * - For Android-only modules: Adds all Koin dependencies directly
 *
 * Usage in module's build.gradle.kts:
 * ```
 * plugins {
 *     alias(libs.plugins.convention.library.koin)
 * }
 * ```
 */
class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Check if this is a KMM module
            val isKmmModule = pluginManager.hasPlugin("org.jetbrains.kotlin.multiplatform")

            if (isKmmModule) {
                // KMM module: Configure via Kotlin Multiplatform extension
                extensions.configure<KotlinMultiplatformExtension> {
                    sourceSets.apply {
                        // Common dependencies
                        getByName("commonMain").dependencies {
                            // Koin BOM for version management
                            implementation(
                                dependencies.platform(
                                    libs.findLibrary("koin.bom").get()
                                )
                            )

                            // Koin Core
                            implementation(libs.findLibrary("koin.core").get())
                        }

                        // Android-specific dependencies
                        getByName("androidMain").dependencies {
                            implementation(libs.findLibrary("koin.android").get())
                        }

                        // Common test dependencies
                        getByName("commonTest").dependencies {
                            implementation(libs.findLibrary("koin.test").get())
                        }
                    }
                }
            } else {
                // Regular Android module: Add all dependencies directly
                dependencies {
                    // Koin BOM for version management
                    "implementation"(platform(libs.findLibrary("koin.bom").get()))

                    // Koin Core
                    "implementation"(libs.findLibrary("koin.core").get())

                    // Koin Android support
                    "implementation"(libs.findLibrary("koin.android").get())

                    // Koin - Compose integration
                    "implementation"(libs.findLibrary("koin.androidx.compose").get())

                    // Testing dependencies
                    "testImplementation"(libs.findLibrary("koin.test").get())
                    "testImplementation"(libs.findLibrary("koin.test.junit4").get())

                    "androidTestImplementation"(libs.findLibrary("koin.android.test").get())
                    "androidTestImplementation"(libs.findLibrary("koin.test.junit4").get())
                }
            }
        }
    }
}
