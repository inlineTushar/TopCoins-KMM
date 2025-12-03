plugins {
    alias(libs.plugins.local.library.composeview)
    alias(libs.plugins.local.library.test)
    alias(libs.plugins.local.library.uitest)
}

android {
    namespace = "com.tushar.ui"

    // Add UI testing configuration
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
}

dependencies {
    implementation(libs.androidx.navigation.compose)
}
