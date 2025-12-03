package com.tushar.plugin

import com.android.build.api.dsl.ApplicationExtension
import com.tushar.ext.configureKotlin
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class ApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.android")
            apply(plugin = "android.lint")  // Apply lint to application module

            extensions.configure<ApplicationExtension> {
                configureAppKotlinAndroid(this)
                configureAppSpecifics(this)
            }
        }
    }
}

private fun Project.configureAppKotlinAndroid(appExtension: ApplicationExtension) {
    appExtension.apply {
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
    }
    configureKotlin<KotlinAndroidProjectExtension>()
}

/**
 * Configure app-specific options
 */
private fun Project.configureAppSpecifics(appExtension: ApplicationExtension) {
    appExtension.apply {
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
}
