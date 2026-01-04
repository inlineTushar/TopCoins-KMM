package com.tushar.plugin

import com.tushar.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin that adds dependency injection to library modules.
 *
 * Usage in module's build.gradle.kts:
 * ```
 * plugins {
 *     alias(libs.plugins.convention.library.hilt)
 * }
 * ```
 */
class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("kotlin-kapt")
                apply("com.google.dagger.hilt.android")
            }

            dependencies {
                // Dependency Injection
                "implementation"(libs.findLibrary("hilt.android").get())
                "kapt"(libs.findLibrary("hilt.compiler").get())

                // Hilt - Compose integration
                "implementation"(libs.findLibrary("hilt.navigation.compose").get())

                // Testing dependencies
                "testImplementation"(libs.findLibrary("hilt.android.testing").get())
                "kaptTest"(libs.findLibrary("hilt.compiler").get())

                "androidTestImplementation"(libs.findLibrary("hilt.android.testing").get())
                "kaptAndroidTest"(libs.findLibrary("hilt.compiler").get())
            }
        }
    }
}
