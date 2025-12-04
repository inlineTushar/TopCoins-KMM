package com.tushar.plugin

import com.android.build.api.dsl.ApplicationExtension
import com.tushar.ext.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for application modules with Kotlin Multiplatform support.
 *
 * This plugin provides a complete application setup with KMM support:
 *
 * Structure:
 * - Applies `com.android.application` for Android app configuration
 * - Applies `org.jetbrains.kotlin.multiplatform` for KMM structure
 * - Applies `org.jetbrains.kotlin.plugin.compose` for Compose compiler
 * - Applies `org.jetbrains.compose` for Compose Multiplatform
 * - Applies `android.library.koin` for Koin dependency injection
 * - Applies `android.lint` for code quality checks
 * - Configures Android and iOS targets
 * - Sets up proper build types (debug/release)
 * - Automatically includes common and feature module dependencies
 *
 * Usage:
 * ```kotlin
 * plugins {
 *     alias(libs.plugins.local.application)
 * }
 *
 * android {
 *     namespace = "com.yourcompany.app"
 *     defaultConfig {
 *         versionCode = 1
 *         versionName = "1.0"
 *     }
 * }
 *
 * // Optional: Configure Compose resources
 * compose {
 *     resources {
 *         packageOfResClass = "com.yourcompany.app.generated.resources"
 *         generateResClass = always
 *     }
 * }
 * ```
 */
class ApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply required plugins
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("org.jetbrains.kotlin.plugin.compose")  // Compose compiler
                apply("org.jetbrains.compose")  // Compose Multiplatform
                apply("android.lint")  // Apply lint to application module
            }

            // Configure Kotlin Multiplatform (includes source sets and dependencies)
            extensions.configure<KotlinMultiplatformExtension> {
                configureKotlinMultiplatform(target)
            }

            // Configure Android Application
            extensions.configure<ApplicationExtension> {
                configureAppKotlinAndroid()
                configureAppSpecifics()
            }

            logger.lifecycle("[${target.name}] ✓ Application module configured with Kotlin Multiplatform")
        }
    }
}

/**
 * Configure Kotlin Multiplatform targets and source sets for application
 */
private fun KotlinMultiplatformExtension.configureKotlinMultiplatform(project: Project) {
    // Android target
    androidTarget {
        compilations.all {
            compilerOptions.configure {
                jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
            }
        }
    }

    // iOS targets (optional - can be enabled if needed)
    val iosTargets = listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    // Configure iOS frameworks
    iosTargets.forEach { target ->
        target.binaries.framework {
            baseName = project.name
            isStatic = true
        }
    }

    // Source sets configuration with dependencies
    sourceSets.apply {
        // Common source set with Compose and module dependencies
        getByName("commonMain") {
            dependencies {
                // Compose Multiplatform core
                val composeVersion = "1.7.3"
                implementation("org.jetbrains.compose.runtime:runtime:$composeVersion")
                implementation("org.jetbrains.compose.foundation:foundation:$composeVersion")
                implementation("org.jetbrains.compose.material3:material3:$composeVersion")
                implementation("org.jetbrains.compose.ui:ui:$composeVersion")
                implementation("org.jetbrains.compose.components:components-resources:$composeVersion")
                implementation("org.jetbrains.compose.components:components-ui-tooling-preview:$composeVersion")

                // Koin BOM for version management
                implementation(
                    project.dependencies.platform(
                        project.libs.findLibrary("koin.bom").get()
                    )
                )

                // Koin Core
                implementation(project.libs.findLibrary("koin.core").get())
                implementation(project.libs.findLibrary("koin.compose").get())
                implementation(project.libs.findLibrary("koin.compose.viewmodel").get())
            }
        }

        getByName("commonTest") {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        // Android source set with Android-specific dependencies
        getByName("androidMain") {
            dependencies {
                // Compose tooling
                val composeVersion = "1.7.3"
                implementation("org.jetbrains.compose.ui:ui-tooling-preview:$composeVersion")
                implementation("org.jetbrains.compose.ui:ui-tooling:$composeVersion")

                // Android Activity Compose
                implementation(project.libs.findLibrary("androidx-activity-compose").get())
                implementation(project.libs.findLibrary("androidx-material-icons-extended").get())
                implementation(project.libs.findLibrary("androidx-navigation-compose").get())

                // Koin Android
                implementation(project.libs.findLibrary("koin.android").get())
                implementation(project.libs.findLibrary("koin.androidx.compose").get())
            }
        }

        // iOS source set (common for all iOS targets)
        val iosMain = create("iosMain") {
            dependsOn(getByName("commonMain"))
        }

        val iosTest = create("iosTest") {
            dependsOn(getByName("commonTest"))
        }

        // Link iOS target-specific source sets to shared iOS source set
        getByName("iosX64Main").dependsOn(iosMain)
        getByName("iosArm64Main").dependsOn(iosMain)
        getByName("iosSimulatorArm64Main").dependsOn(iosMain)

        getByName("iosX64Test").dependsOn(iosTest)
        getByName("iosArm64Test").dependsOn(iosTest)
        getByName("iosSimulatorArm64Test").dependsOn(iosTest)
    }

    project.logger.lifecycle("[${project.name}] ✓ Multiplatform dependencies configured")
}

/**
 * Configure Android application settings
 */
private fun ApplicationExtension.configureAppKotlinAndroid() {
    compileSdk = 36

    defaultConfig {
        // Use namespace as default applicationId if not already set
        // This will be overridden if explicitly set in the app module's build.gradle.kts
        applicationId = namespace
        minSdk = 26
        targetSdk = 35
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Enable build features
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
 * Configure app-specific options (build types)
 */
private fun ApplicationExtension.configureAppSpecifics() {
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = false
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = true
        }
    }
}


