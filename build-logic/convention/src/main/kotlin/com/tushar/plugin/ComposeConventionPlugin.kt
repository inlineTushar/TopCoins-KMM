package com.tushar.plugin

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.tushar.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for Compose UI setup.
 *
 * Automatically detects module type and configures:
 * - Compose Multiplatform for KMM modules (applies plugin + dependencies)
 * - AndroidX Compose for Android-only modules
 *
 * Usage in module's build.gradle.kts:
 * ```
 * plugins {
 *     alias(libs.plugins.local.library.composeview)
 * }
 * ```
 */
class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Always apply Compose compiler plugin
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")


            // Check if this is a KMM module
            val isKmmModule = pluginManager.hasPlugin("org.jetbrains.kotlin.multiplatform")

            if (isKmmModule) {
                // KMM module: Use Compose Multiplatform
                logger.lifecycle("[$name] Configuring Compose Multiplatform")

                // Apply Compose Multiplatform plugin (available from root build.gradle.kts)
                apply(plugin = "org.jetbrains.compose")

                // Apply multiplatform plugin if not already applied
                if (!pluginManager.hasPlugin("android.multiplatform.conversion")) {
                    apply(plugin = "android.multiplatform.conversion")
                }

                configureComposeMultiplatform()
            } else {
                // Android-only module: Use AndroidX Compose
                logger.lifecycle("[$name] Configuring AndroidX Compose")

                // Check if this is an application or library module
                val isApplication = pluginManager.hasPlugin("com.android.application")

                if (!isApplication) {
                    // Only apply android.library if it's not an application
                    apply(plugin = "android.library")
                }

                // Get the appropriate extension
                val extension = if (isApplication) {
                    extensions.getByType<ApplicationExtension>()
                } else {
                    extensions.getByType<LibraryExtension>()
                }

                configureCompose(extension, isApplication)
            }
        }
    }
}

/**
 * Configure Compose Multiplatform for KMM modules
 */
internal fun Project.configureComposeMultiplatform() {
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.apply {
            // Common Compose Multiplatform dependencies
            getByName("commonMain").dependencies {
                // Compose Multiplatform dependencies (using dependency strings directly)
                val composeVersion = "1.7.3"
                implementation("org.jetbrains.compose.runtime:runtime:$composeVersion")
                implementation("org.jetbrains.compose.foundation:foundation:$composeVersion")
                implementation("org.jetbrains.compose.material3:material3:$composeVersion")
                implementation("org.jetbrains.compose.ui:ui:$composeVersion")
                implementation("org.jetbrains.compose.components:components-resources:$composeVersion")
                implementation("org.jetbrains.compose.components:components-ui-tooling-preview:$composeVersion")

                // Collections immutable for Compose performance
                implementation(libs.findLibrary("kotlinx-collections-immutable").get())
            }

            // Android-specific Compose additions
            getByName("androidMain").dependencies {
                // Preview and tooling
                val composeVersion = "1.7.3"
                implementation("org.jetbrains.compose.ui:ui-tooling-preview:$composeVersion")
                implementation("org.jetbrains.compose.ui:ui-tooling:$composeVersion")

                // Android Activity Compose
                implementation(libs.findLibrary("androidx-activity-compose").get())
                implementation(libs.findLibrary("androidx-material-icons-extended").get())

                // Navigation Compose (for feature modules and common:ui)
                if (path.contains(":feature:") || path.contains(":common:ui")) {
                    implementation(libs.findLibrary("androidx-navigation-compose").get())
                }
            }

            // iOS uses dependencies from commonMain - no additional dependencies needed
        }
    }
}

/**
 * Configure AndroidX Compose for Android-only modules
 *
 * @param commonExtension The Android extension (LibraryExtension or ApplicationExtension)
 * @param isApplication true if this is an application module, false for library
 */
internal fun Project.configureCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    isApplication: Boolean = false
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        testOptions {
            unitTests {
                // For Robolectric
                isIncludeAndroidResources = true
            }
        }
    }

    dependencies {
        val composeBom = libs.findLibrary("androidx-compose-bom").get()

        // Compose BOM for version management
        "implementation"(platform(composeBom))

        // Compose UI dependencies
        "implementation"(libs.findLibrary("androidx-ui").get())
        "implementation"(libs.findLibrary("androidx-ui-graphics").get())
        "implementation"(libs.findLibrary("androidx-ui-tooling-preview").get())

        // Material3
        "implementation"(libs.findLibrary("androidx.material3").get())

        // Activity Compose (useful for both libraries and apps)
        "implementation"(libs.findLibrary("androidx-activity-compose").get())

        // Navigation Compose (for apps and feature modules)
        if (isApplication || path.contains(":feature:") || path.contains(":common:navigation")) {
            "implementation"(libs.findLibrary("androidx-navigation-compose").get())
        }

        // Material Icons Extended (for full icon support including AutoMirrored icons)
        "implementation"(libs.findLibrary("androidx-material-icons-extended").get())

        // Kotlinx Collections Immutable for Compose performance optimization
        "implementation"(libs.findLibrary("kotlinx-collections-immutable").get())

        // Debug tooling
        "debugImplementation"(libs.findLibrary("androidx-ui-tooling").get())
        "debugImplementation"(libs.findLibrary("androidx-ui-test-manifest").get())
    }
}
