package com.tushar.plugin

import com.android.build.gradle.LibraryExtension
import com.tushar.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin for feature modules
 *
 * Provides:
 * - Common feature module dependencies (navigation, core, ui, data, domain)
 * - Compose configuration
 * - Test (Unit and UI) configuration
 */
class LibraryFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "android.library")
            apply(plugin = "android.library.koin")
            apply(plugin = "android.library.test")
            apply(plugin = "android.library.uitest")
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")

            extensions.configure<LibraryExtension> {
                testOptions.animationsDisabled = true

                // Enable proper Android unit testing configuration
                testOptions {
                    unitTests {
                        isIncludeAndroidResources = true
                    }
                }

                buildFeatures {
                    // Enables Jetpack Compose for this module
                    compose = true
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                        excludes += "/META-INF/LICENSE.md"
                        excludes += "/META-INF/LICENSE-notice.md"
                        excludes += "/META-INF/NOTICE.md"
                    }
                }
            }

            dependencies {
                "implementation"(libs.findLibrary("androidx.navigation.compose").get())
                "implementation"(project(":common:navigation"))
                "implementation"(project(":common:ui"))
                "implementation"(project(":common:data"))
                "implementation"(project(":common:domain"))
            }
        }
    }
}
