package com.tushar.plugin

import com.android.build.gradle.LibraryExtension
import com.tushar.ext.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin that configures a module for Kotlin Multiplatform.
 *
 * This plugin provides the STRUCTURAL setup for KMM without opinionated dependencies.
 * Each module should declare its own dependencies based on its specific needs.
 *
 * This plugin enables:
 * - Android and iOS targets
 * - Shared source sets (commonMain, androidMain, iosMain, with proper linking)
 * - Proper Android library configuration
 * - iOS framework generation
 *
 * Usage in module's build.gradle.kts:
 * ```
 * plugins {
 *     alias(libs.plugins.convention.multiplatform)
 * }
 * ```
 */
class MultiplatformConversionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            // Configure Kotlin Multiplatform
            extensions.configure<KotlinMultiplatformExtension> {
                configureKotlinMultiplatform(target)
            }

            // Configure Android Library
            extensions.configure<LibraryExtension> {
                configureAndroidLibrary()
            }

            // Configure common dependencies
            configureDependencies()
        }
    }
}

/**
 * Configure Kotlin Multiplatform targets and source sets
 */
private fun KotlinMultiplatformExtension.configureKotlinMultiplatform(project: Project) {
    androidTarget {
        compilations.all {
            compilerOptions.configure {
                jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
            }
        }
    }

    val iosTargets = listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    val libs = project.libs

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

/**
 * Configure Android library settings for multiplatform
 */
private fun LibraryExtension.configureAndroidLibrary() {
    compileSdk = 36

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Enable build features if needed
    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }
}

/**
 * Configure common dependencies for multiplatform modules
 *
 * Note: This method is intentionally left minimal. Each module should declare
 * its own dependencies based on its specific needs. This keeps the plugin
 * flexible and non-opinionated about what libraries to use.
 */
private fun Project.configureDependencies() {
    dependencies {}
}
