package com.tushar.plugin

import com.android.build.gradle.LibraryExtension
import com.tushar.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin

/**
 * Convention plugin that configures a module for Kotlin Multiplatform.
 *
 * This plugin provides the STRUCTURAL setup for KMM without opinionated dependencies.
 * Each module should declare its own dependencies based on its specific needs.
 *
 * This plugin enables:
 * - Android and iOS targets with default SDK configuration (compileSdk, minSdk)
 * - Shared source sets (commonMain, androidMain, iosMain, with proper linking)
 * - iOS framework generation
 *
 * Note: Each module must configure its own namespace via android { namespace = "..." }
 *
 * Usage in module's build.gradle.kts:
 * ```
 * plugins {
 *     alias(libs.plugins.convention.multiplatform)
 * }
 *
 * android {
 *     namespace = "com.example.module"
 * }
 * ```
 */
class MultiplatformConversionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = libs

            val compileSdkVersion = libs.findVersion("compileSdk").get().toString().toInt()
            val minSdkVersion = libs.findVersion("minSdk").get().toString().toInt()

            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            // Configure Android SDK defaults
            extensions.configure<LibraryExtension> {
                compileSdk = compileSdkVersion
                defaultConfig.minSdk = minSdkVersion
            }

            // Configure Kotlin Multiplatform targets (Android + iOS)
            plugins.withType<KotlinBasePlugin> {
                extensions.configure<KotlinMultiplatformExtension> {
                    // Configure targets and source sets
                    configureKotlinMultiplatform(target)
                }
            }
        }
    }
}

/**
 * Configure Kotlin Multiplatform targets and source sets.
 */
private fun KotlinMultiplatformExtension.configureKotlinMultiplatform(project: Project) {
    val libs = project.libs

    androidTarget()

    val iosTargets = listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    // Configure iOS frameworks
    iosTargets.forEach { target ->
        target.binaries.framework {
            export(libs.findLibrary("androidx-lifecycle-viewmodel").get())
            baseName = project.name.replaceFirstChar { it.uppercase() }
            isStatic = true
        }
    }

    sourceSets.apply {
        getByName("commonMain") {
            dependencies {
                implementation(libs.findLibrary("kotlinx-coroutines-core").get())
            }
        }
    }
}
