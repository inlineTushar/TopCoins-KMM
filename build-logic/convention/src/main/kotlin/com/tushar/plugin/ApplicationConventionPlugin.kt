package com.tushar.plugin

import com.android.build.api.dsl.ApplicationExtension
import com.tushar.ext.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class ApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = libs

            // Get SDK versions from version catalog
            val compileSdk = libs.findVersion("compileSdk").get().toString().toInt()
            val minSdk = libs.findVersion("minSdk").get().toString().toInt()
            val targetSdk = libs.findVersion("targetSdk").get().toString().toInt()

            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("android.lint")
            }

            // Configure Kotlin Android
            extensions.configure<KotlinAndroidProjectExtension> {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_11)
                }
            }

            // Configure Android Application
            extensions.configure<ApplicationExtension> {
                configureAppKotlinAndroid(compileSdk, minSdk, targetSdk)
                configureAppSpecifics()
                configureBuildFeatures()
            }

            // Configure dependencies
            configureDependencies()

            logger.lifecycle("[${target.name}] ✓ Application module configured (Android-only)")
        }
    }
}

/**
 * Configure Android application settings
 */
private fun ApplicationExtension.configureAppKotlinAndroid(
    compile: Int,
    min: Int,
    target: Int
) {
    compileSdk = compile

    defaultConfig {
        applicationId = namespace
        minSdk = min
        targetSdk = target
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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
 * Configure build features
 */
private fun ApplicationExtension.configureBuildFeatures() {
    buildFeatures {
        buildConfig = true
        compose = true
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

/**
 * Configure dependencies for Android application
 */
private fun Project.configureDependencies() {
    dependencies {
        // AndroidX Compose BOM
        add("implementation", platform(libs.findLibrary("androidx.compose.bom").get()))
        add("implementation", libs.findLibrary("androidx.ui").get())
        add("implementation", libs.findLibrary("androidx.ui.graphics").get())
        add("implementation", libs.findLibrary("androidx.ui.tooling.preview").get())
        add("implementation", libs.findLibrary("androidx.material3").get())
        add("debugImplementation", libs.findLibrary("androidx.ui.tooling").get())

        // Android Activity Compose
        add("implementation", libs.findLibrary("androidx.activity.compose").get())
        add("implementation", libs.findLibrary("androidx.material.icons.extended").get())

        // Koin
        add("implementation", platform(libs.findLibrary("koin.bom").get()))
        add("implementation", libs.findLibrary("koin.core").get())
        add("implementation", libs.findLibrary("koin.android").get())
        add("implementation", libs.findLibrary("koin.androidx.compose").get())
    }
}
