package com.tushar.plugin

import com.tushar.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin for Android UI testing dependencies.
 * Applied to modules that use LibraryComposeConventionPlugin or LibraryXMLViewConventionPlugin.
 *
 * Provides:
 * - AndroidX JUnit
 * - Espresso Core
 * - MockK Android
 * - Compose UI Test dependencies (when applicable)
 *
 * Note: This plugin is skipped for modules using the new com.android.kotlin.multiplatform.library
 * plugin, as it uses different test configurations.
 */
class UITestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Check if this is a KMP module with the new Android plugin
            // The new plugin doesn't provide androidTestImplementation configuration
            afterEvaluate {
                val hasAndroidTestConfig = configurations.findByName("androidTestImplementation") != null

                if (hasAndroidTestConfig) {
                    dependencies {
                        // Compose BOM for UI tests (provides consistent Compose versions)
                        val composeBom = libs.findLibrary("androidx-compose-bom").get()
                        "androidTestImplementation"(platform(composeBom))

                        // AndroidX JUnit (Android test runner)
                        "androidTestImplementation"(libs.findLibrary("androidx.junit").get())

                        // Espresso Core (UI testing framework)
                        "androidTestImplementation"(libs.findLibrary("androidx.espresso.core").get())

                        // MockK Android (Mocking for Android instrumented tests)
                        "androidTestImplementation"(libs.findLibrary("mockk.android").get())

                        // Compose UI Test JUnit4 (if Compose is used)
                        // Note: This will work even if Compose isn't used, it just won't be utilized
                        "androidTestImplementation"(libs.findLibrary("androidx.ui.test.junit4").get())

                        // Debug dependencies for UI tests
                        "debugImplementation"(libs.findLibrary("androidx.ui.tooling").get())
                        "debugImplementation"(libs.findLibrary("androidx.ui.test.manifest").get())
                    }
                } else {
                    logger.lifecycle("[${name}] Skipping UI test configuration (KMP module with new Android plugin)")
                }
            }
        }
    }
}
