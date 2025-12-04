package com.tushar.plugin

import com.tushar.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin that adds Koin dependency injection to library modules.
 *
 * Unlike Hilt, Koin doesn't require annotation processing (kapt/ksp) as it uses
 * runtime dependency injection. This plugin adds all necessary Koin dependencies
 * including core, Android support, Compose integration, and testing libraries.
 *
 * Usage in module's build.gradle.kts:
 * ```
 * plugins {
 *     alias(libs.plugins.android.library)
 *     alias(libs.plugins.library.koin)
 * }
 * ```
 */
class LibraryKoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
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
