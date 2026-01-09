package com.tushar.plugin

import com.tushar.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
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
 * Note: Each module must configure its own namespace via kotlin { androidLibrary { namespace = "..." } }
 *
 * Usage in module's build.gradle.kts:
 * ```
 * plugins {
 *     alias(libs.plugins.convention.multiplatform)
 * }
 *
 * kotlin {
 *     androidLibrary {
 *         namespace = "com.example.module"
 *         // compileSdk and minSdk are configured by the convention plugin
 *     }
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
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            // Configure Kotlin Multiplatform targets (iOS) and Android SDK defaults
            plugins.withType<KotlinBasePlugin> {
                extensions.configure<KotlinMultiplatformExtension> {
                    // Configure Android SDK defaults using Groovy interop
                    configureAndroidLibrary(compileSdkVersion, minSdkVersion)

                    // Configure iOS targets and source sets
                    configureKotlinMultiplatform(target)
                }
            }
        }
    }
}

/**
 * Configure Android library defaults using Groovy interop.
 * The androidLibrary {} block is added dynamically by the KMP Android plugin.
 */
private fun KotlinMultiplatformExtension.configureAndroidLibrary(compileSdk: Int, minSdk: Int) {
    (this as groovy.lang.GroovyObject).invokeMethod(
        "androidLibrary",
        closureOf<Any> {
            (this as groovy.lang.GroovyObject).setProperty("compileSdk", compileSdk)
            (this as groovy.lang.GroovyObject).setProperty("minSdk", minSdk)
        }
    )
}

/**
 * Helper function to create a Groovy closure from a Kotlin lambda.
 */
private fun <T> Any.closureOf(action: T.() -> Unit): groovy.lang.Closure<Unit> =
    object : groovy.lang.Closure<Unit>(this) {
        @Suppress("UNCHECKED_CAST")
        override fun call(): Unit = (delegate as T).action()
    }

/**
 * Configure Kotlin Multiplatform targets and source sets.
 */
private fun KotlinMultiplatformExtension.configureKotlinMultiplatform(project: Project) {
    val libs = project.libs

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
