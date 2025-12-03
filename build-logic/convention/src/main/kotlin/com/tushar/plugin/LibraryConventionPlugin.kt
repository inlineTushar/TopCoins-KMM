package com.tushar.plugin

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.tushar.ext.configureKotlin
import com.tushar.ext.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import java.io.File

class LibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.android")
            apply(plugin = "android.lint")  // Apply lint to all library modules

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 36
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                testOptions.animationsDisabled = true
                buildFeatures {
                    buildConfig = true
                }

                val rawResDir = File(project.projectDir, "src/main/res/raw")
                val hasPropertiesFile = rawResDir.takeIf { it.isDirectory }
                    ?.listFiles { file -> file.extension == "properties" }
                    ?.isNotEmpty() == true

                if (!hasPropertiesFile) {
                    // The resource prefix is derived from the module name,
                    // so resources inside ":core:module1" must be prefixed with "core_module1_"
                    // skipping properties files from this rule
                    resourcePrefix =
                        path
                            .split("""\W""".toRegex())
                            .drop(1)
                            .distinct()
                            .joinToString(separator = "_")
                            .lowercase() + "_"
                }
            }

            dependencies {
                "api"(libs.findLibrary("kotlinx.datetime").get())
                "api"(libs.findLibrary("timber").get())
            }
        }
    }
}

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        compileSdk = 36

        defaultConfig {
            minSdk = 26
        }

        compileOptions {
            // Up to Java 11 APIs are available through desugaring
            // https://developer.android.com/studio/write/java11-minimal-support-table
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }
    configureKotlin<KotlinAndroidProjectExtension>()
}
