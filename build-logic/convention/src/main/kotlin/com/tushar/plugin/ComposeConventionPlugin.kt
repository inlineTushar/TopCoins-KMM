package com.tushar.plugin

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.tushar.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.closureOf
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
 *     alias(libs.plugins.convention.library.compose)
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
                    // Only apply android library if it's not an application
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
        (this as groovy.lang.GroovyObject).invokeMethod(
            "androidLibrary",
            closureOf<Any> {
                (this as groovy.lang.GroovyObject).invokeMethod(
                    "androidResources",
                    closureOf<Any> {
                        (this as groovy.lang.GroovyObject).setProperty("enable", true)
                    }
                )
            }
        )
        sourceSets.apply {
            getByName("commonMain").dependencies {
                implementation(libs.findLibrary("compose-multiplatform-runtime").get())
                implementation(libs.findLibrary("compose-multiplatform-foundation").get())
                implementation(libs.findLibrary("compose-multiplatform-material3").get())
                implementation(libs.findLibrary("compose-multiplatform-ui").get())
                implementation(libs.findLibrary("compose-multiplatform-components-resources").get())
                implementation(libs.findLibrary("compose-multiplatform-components-ui-tooling-preview").get())
                implementation(libs.findLibrary("kotlinx-collections-immutable").get())
                implementation(libs.findLibrary("compose-multiplatform-icons-extended").get())
            }

            getByName("androidMain").dependencies {
                implementation(libs.findLibrary("compose-multiplatform-ui-tooling-preview").get())
                implementation(libs.findLibrary("compose-multiplatform-ui-tooling").get())
                implementation(libs.findLibrary("androidx-activity-compose").get())
            }
        }
    }
}

/**
 * Helper function to create a Groovy closure from a Kotlin lambda.
 */

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
}
