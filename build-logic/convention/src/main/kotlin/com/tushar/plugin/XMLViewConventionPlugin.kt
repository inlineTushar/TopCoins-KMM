package com.tushar.plugin

import com.android.build.api.dsl.CommonExtension
import com.tushar.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class XMLViewConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

            // Get the common extension (works for both Application and Library)
            val extension = project.extensions.findByType(CommonExtension::class.java)
            if (extension != null) {
                configureXMLView(extension)
            }
        }
    }
}

/**
 * Configure XML View-specific options and dependencies
 */
internal fun Project.configureXMLView(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        buildFeatures {
            viewBinding = true
        }

        dependencies {
            // AndroidX Core
            "implementation"(libs.findLibrary("androidx-core-ktx").get())
            "implementation"(libs.findLibrary("androidx-appcompat").get())
            "implementation"(libs.findLibrary("google-material").get())
            "implementation"(libs.findLibrary("androidx-constraintlayout").get())
            "implementation"(libs.findLibrary("androidx-recyclerview").get())

            // Navigation
            "implementation"(libs.findLibrary("androidx-navigation-fragment-ktx").get())
            "implementation"(libs.findLibrary("androidx-navigation-ui-ktx").get())

            // Lifecycle Components
            "implementation"(libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())

            // Networking
            "implementation"(libs.findLibrary("retrofit").get())
            "implementation"(libs.findLibrary("retrofit-kotlinx-serialization").get())
            "implementation"(libs.findLibrary("kotlinx-serialization-json").get())

            // Coroutines
            "implementation"(libs.findLibrary("kotlinx-coroutines-core").get())
            "implementation"(libs.findLibrary("kotlinx-coroutines-android").get())
        }

        testOptions {
            unitTests {
                // For Robolectric
                isIncludeAndroidResources = true
            }
        }
    }
}
