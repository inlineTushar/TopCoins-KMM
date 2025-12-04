plugins {
    alias(libs.plugins.local.application)
    alias(libs.plugins.local.library.composeview)
    alias(libs.plugins.local.library.koin)
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
            applicationIdSuffix = ".topcoin.debug"
            signingConfig = signingConfigs.getByName("debugSign")
        }
        release {
            applicationIdSuffix = ".topcoin.release"
            signingConfig = signingConfigs.getByName("releaseSign")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    // Feature modules
    implementation(project(":feature:coinlist"))

    // Common modules
    implementation(project(":common:navigation"))
    implementation(project(":common:ui"))
    implementation(project(":common:data"))
    implementation(project(":common:domain"))
}
