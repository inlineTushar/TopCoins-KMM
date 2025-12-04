plugins {
    alias(libs.plugins.local.application)
}

android {
    namespace = "com.tushar.topcoins"

    defaultConfig {
        versionCode = 1
        versionName = "1.0"
    }

    signingConfigs {
        create("debugSign") {
            storeFile = file("${rootDir}/keystore/debug.keystore.jks")
            keyAlias = "debugKey"
            keyPassword = "android"
            storePassword = "android"
        }
        create("releaseSign") {
            storeFile = file("${rootDir}/keystore/release.keystore.jks")
            // source from /Users/{user.name}/gradle.properties
            keyAlias = project.findProperty("COIN_RELEASE_KEY_ALIAS") as String?
                ?: throw GradleException("COIN_RELEASE_KEY_ALIAS property not found in gradle.properties")
            keyPassword = project.findProperty("COIN_RELEASE_KEY_PASSWORD") as String?
                ?: throw GradleException("COIN_RELEASE_KEY_PASSWORD property not found in gradle.properties")
            storePassword = project.findProperty("COIN_RELEASE_STORE_PASSWORD") as String?
                ?: throw GradleException("COIN_RELEASE_STORE_PASSWORD property not found in gradle.properties")
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".kmm.debug"
            signingConfig = signingConfigs.getByName("debugSign")
        }
        release {
            applicationIdSuffix = ".kmm.release"
            signingConfig = signingConfigs.getByName("releaseSign")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

compose {
    resources {
        packageOfResClass = "com.tushar.topcoins.generated.resources"
        generateResClass = always
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                // Common modules - these are shared across all platforms
                implementation(project(":common:ui"))
                implementation(project(":common:data"))
                implementation(project(":common:domain"))

                // Feature modules
                implementation(project(":feature:coinlist"))
            }
        }

        androidMain {
            dependencies {
                // Android-only common modules
                implementation(project(":common:navigation"))
            }
        }
    }
}
