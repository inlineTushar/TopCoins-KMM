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
 * - Hilt Android Testing
 * - Compose UI Test dependencies (when applicable)
 */
class LibraryUITestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
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

                // Hilt Android Testing (DI for tests)
                "androidTestImplementation"(libs.findLibrary("hilt.android.testing").get())

                // Compose UI Test JUnit4 (if Compose is used)
                // Note: This will work even if Compose isn't used, it just won't be utilized
                "androidTestImplementation"(libs.findLibrary("androidx.ui.test.junit4").get())

                // Debug dependencies for UI tests
                "debugImplementation"(libs.findLibrary("androidx.ui.tooling").get())
                "debugImplementation"(libs.findLibrary("androidx.ui.test.manifest").get())
            }
        }
    }
}
