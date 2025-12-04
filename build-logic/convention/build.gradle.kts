import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.tushar.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.compose.multiplatform.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("application") {
            id = libs.plugins.local.application.get().pluginId
            implementationClass = "com.tushar.plugin.ApplicationConventionPlugin"
        }
        register("library") {
            id = libs.plugins.local.library.asProvider().get().pluginId
            implementationClass = "com.tushar.plugin.LibraryConventionPlugin"
        }
        register("libraryXMLView") {
            id = libs.plugins.local.library.xmlview.get().pluginId
            implementationClass = "com.tushar.plugin.LibraryXMLViewConventionPlugin"
        }
        register("libraryComposeView") {
            id = libs.plugins.local.library.composeview.get().pluginId
            implementationClass = "com.tushar.plugin.LibraryComposeConventionPlugin"
        }
        register("libraryHilt") {
            id = libs.plugins.local.library.hilt.get().pluginId
            implementationClass = "com.tushar.plugin.LibraryHiltConventionPlugin"
        }
        register("libraryKoin") {
            id = libs.plugins.local.library.koin.get().pluginId
            implementationClass = "com.tushar.plugin.LibraryKoinConventionPlugin"
        }
        register("libraryFeature") {
            id = libs.plugins.local.library.feature.get().pluginId
            implementationClass = "com.tushar.plugin.LibraryFeatureConventionPlugin"
        }
        register("libraryTest") {
            id = libs.plugins.local.library.test.get().pluginId
            implementationClass = "com.tushar.plugin.LibraryTestConventionPlugin"
        }
        register("libraryUITest") {
            id = libs.plugins.local.library.uitest.get().pluginId
            implementationClass = "com.tushar.plugin.LibraryUITestConventionPlugin"
        }
        register("lint") {
            id = libs.plugins.local.lint.get().pluginId
            implementationClass = "com.tushar.plugin.LintConventionPlugin"
        }
        register("multiplatformConversion") {
            id = libs.plugins.local.multiplatform.get().pluginId
            implementationClass = "com.tushar.plugin.MultiplatformConversionPlugin"
        }
    }
}