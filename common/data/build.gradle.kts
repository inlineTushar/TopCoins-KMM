import java.util.Properties

plugins {
    alias(libs.plugins.convention.multiplatform)
    alias(libs.plugins.convention.encryption)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.convention.library.koin)
    alias(libs.plugins.convention.library.test)
}

buildkonfig {
    packageName = "com.tushar.common.data"

    defaultConfigs {
        val keyPropsFile = rootProject.file("secret/key.properties")
        if (keyPropsFile.exists()) {
            val properties = Properties()
            keyPropsFile.inputStream().use { properties.load(it) }
            properties.forEach { (key, value) ->
                val cleanValue = value.toString().removeSurrounding("\"")
                buildConfigField(
                    com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
                    key.toString(),
                    cleanValue
                )
            }
        }
    }
}

android {
    namespace = "com.tushar.common.data"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":common:core"))
            implementation(project(":common:domain"))

            api(libs.ktor.client.core)
            api(libs.ktor.client.content.negotiation)
            api(libs.ktor.serialization.kotlinx.json)
            api(libs.ktor.client.logging)
            api(libs.ktor.client.auth)

            // Serialization
            api(libs.kotlinx.serialization.json)
        }

        androidMain.dependencies {
            // Android-specific Ktor engine
            implementation(libs.ktor.client.okhttp)
        }

        iosMain.dependencies {
            // iOS-specific Ktor engine
            implementation(libs.ktor.client.darwin)
        }
    }
}
