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
    compileOnly(libs.mokkery.gradlePlugin)
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
            id = libs.plugins.convention.application.get().pluginId
            implementationClass = "com.tushar.plugin.ApplicationConventionPlugin"
        }
        register("library") {
            id = libs.plugins.convention.library.asProvider().get().pluginId
            implementationClass = "com.tushar.plugin.BaseConventionPlugin"
        }
        register("libraryXMLView") {
            id = libs.plugins.convention.library.xmlview.get().pluginId
            implementationClass = "com.tushar.plugin.XMLViewConventionPlugin"
        }
        register("libraryComposeView") {
            id = libs.plugins.convention.library.compose.get().pluginId
            implementationClass = "com.tushar.plugin.ComposeConventionPlugin"
        }
        register("libraryKoin") {
            id = libs.plugins.convention.library.koin.get().pluginId
            implementationClass = "com.tushar.plugin.KoinConventionPlugin"
        }
        register("libraryFeature") {
            id = libs.plugins.convention.library.feature.get().pluginId
            implementationClass = "com.tushar.plugin.FeatureConventionPlugin"
        }
        register("libraryTest") {
            id = libs.plugins.convention.library.test.get().pluginId
            implementationClass = "com.tushar.plugin.TestConventionPlugin"
        }
        register("libraryUITest") {
            id = libs.plugins.convention.library.uitest.get().pluginId
            implementationClass = "com.tushar.plugin.UITestConventionPlugin"
        }
        register("lint") {
            id = libs.plugins.convention.lint.get().pluginId
            implementationClass = "com.tushar.plugin.LintConventionPlugin"
        }
        register("multiplatformConversion") {
            id = libs.plugins.convention.multiplatform.get().pluginId
            implementationClass = "com.tushar.plugin.MultiplatformConversionPlugin"
        }
    }
}