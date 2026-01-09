package com.tushar.plugin

import com.tushar.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for feature modules (Kotlin Multiplatform)
 * This plugin provides a complete feature module setup with KMM support.
 *
 * Uses the new com.android.kotlin.multiplatform.library plugin for Android configuration.
 **/

class FeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "android.multiplatform.conversion")
            apply(plugin = "android.library.composeview")
            apply(plugin = "android.library.koin")
            apply(plugin = "android.library.test")
            apply(plugin = "android.library.uitest")

            // Configure KMM dependencies
            extensions.configure<KotlinMultiplatformExtension> {
                configureFeatureDependencies(this@with)
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
        }

        project.logger.lifecycle("[${project.name}] ✓ Feature module dependencies configured")
    }
}