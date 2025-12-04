package com.tushar.plugin

import com.tushar.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for unit testing dependencies.
 *
 * This plugin is KMM-aware:
 * - For KMM modules: Adds test dependencies to commonTest source set
 *   - MockK is only added to Android tests (not available for iOS native)
 * - For Android-only modules: Adds test dependencies using testImplementation
 *
 * Provides:
 * - kotlin.test (multiplatform test framework)
 * - JUnit 4/5 (Android modules only)
 * - MockK (Android only for KMM)
 * - Coroutines Test
 * - Turbine (for Flow testing)
 * - AssertK
 */
class LibraryTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Check if this is a KMM module
            val isKmmModule = pluginManager.hasPlugin("org.jetbrains.kotlin.multiplatform")

            if (isKmmModule) {
                // KMM module: Configure via Kotlin Multiplatform extension
                extensions.configure<KotlinMultiplatformExtension> {
                    sourceSets.apply {
                        // Common test dependencies (multiplatform compatible)
                        getByName("commonTest").dependencies {
                            // Kotlin test framework (multiplatform)
                            implementation(kotlin("test"))

                            // Coroutines Test
                            implementation(libs.findLibrary("kotlinx.coroutines.test").get())

                            // Turbine (for testing Kotlin Flows)
                            implementation(libs.findLibrary("turbine").get())

                            // AssertK (Fluent assertions for Kotlin)
                            implementation(libs.findLibrary("assertk").get())
                        }

                        // Android-specific test dependencies (MockK only works on JVM/Android)
                        findByName("androidUnitTest")?.dependencies {
                            implementation(libs.findLibrary("mockk").get())
                        }
                    }
                }
            } else {
                // Regular Android module: Add JVM test dependencies
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
}
