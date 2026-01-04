package com.tushar.plugin

import com.android.build.gradle.LibraryExtension
import com.tushar.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for feature modules (Kotlin Multiplatform)
 * This plugin provides a complete feature module setup with KMM support:
**/

class FeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "android.multiplatform.conversion")
            apply(plugin = "android.library.composeview")
            apply(plugin = "android.library.koin")
            apply(plugin = "android.library.test")
            apply(plugin = "android.library.uitest")

            // Configure Android-specific settings
            extensions.configure<LibraryExtension> {
                testOptions.animationsDisabled = true

                // Enable proper Android unit testing configuration
                testOptions {
                    unitTests {
                        isIncludeAndroidResources = true
                    }
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                        excludes += "/META-INF/LICENSE.md"
                        excludes += "/META-INF/LICENSE-notice.md"
                        excludes += "/META-INF/NOTICE.md"
                    }
                }

                // Configure ProGuard/R8 rules at plugin level
                configureProguard(this@with)
            }

            // Configure KMM dependencies
            extensions.configure<KotlinMultiplatformExtension> {
                configureFeatureDependencies(this@with)
            }
        }
    }

    /**
     * Configures ProGuard/R8 rules for feature modules
     */
    private fun LibraryExtension.configureProguard(project: Project) {
        defaultConfig {
            // Consumer ProGuard rules - automatically applied to consuming modules
            val consumerRulesFile = project.file("consumer-rules.pro")
            if (consumerRulesFile.exists()) {
                consumerProguardFiles(consumerRulesFile)
                project.logger.lifecycle("[${project.name}] ✓ Applied consumer-rules.pro")
            }

            // Module-specific ProGuard rules
            val proguardRulesFile = project.file("proguard-rules.pro")
            if (proguardRulesFile.exists()) {
                proguardFiles(proguardRulesFile)
                project.logger.lifecycle("[${project.name}] ✓ Applied proguard-rules.pro")
            }
        }
    }

    /**
     * Configures common feature module dependencies for KMM
     */
    private fun KotlinMultiplatformExtension.configureFeatureDependencies(project: Project) {
        sourceSets.apply {
            getByName("commonMain").dependencies {
                implementation(project.libs.findLibrary("kotlinx-collections-immutable").get())
                implementation(project.libs.findLibrary("androidx-lifecycle-viewmodel").get())
                implementation(project.dependencies.platform(project.libs.findLibrary("koin-bom").get()))
                implementation(project.libs.findLibrary("koin-compose").get())
                implementation(project.libs.findLibrary("koin-compose-viewmodel").get())
                api(project.project(":common:navigation"))
            }

            getByName("androidMain").dependencies {
                implementation(project.libs.findLibrary("koin-androidx-compose").get())
            }

            // iOS dependencies (if needed in the future)
            // getByName("iosMain").dependencies {
            //     // iOS-specific dependencies
            // }
        }

        project.logger.lifecycle("[${project.name}] ✓ Feature module dependencies configured")
    }
}
