package com.tushar.plugin

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.tushar.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class LibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply Compose compiler plugin
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")

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

/**
 * Configure Compose-specific options for both Library and Application modules
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
        if (isApplication || path.contains(":feature:")) {
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
