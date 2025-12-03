package com.tushar.plugin

import com.tushar.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin for JUnit unit testing dependencies.
 * Applied to modules that use LibraryConventionPlugin.
 *
 * Provides:
 * - JUnit 4
 * - JUnit 5 (Jupiter)
 * - MockK
 * - Coroutines Test
 * - Turbine (for Flow testing)
 * - AssertK
 */
class LibraryTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                // JUnit 4 (Android standard)
                "testImplementation"(libs.findLibrary("junit").get())

                // JUnit 5 (Modern testing framework)
                "testImplementation"(libs.findLibrary("junit5").get())

                // MockK (Kotlin mocking library)
                "testImplementation"(libs.findLibrary("mockk").get())

                // Coroutines Test (for testing coroutines)
                "testImplementation"(libs.findLibrary("kotlinx.coroutines.test").get())

                // Turbine (for testing Kotlin Flows)
                "testImplementation"(libs.findLibrary("turbine").get())

                // AssertK (Fluent assertions for Kotlin)
                "testImplementation"(libs.findLibrary("assertk").get())
            }
        }
    }
}
