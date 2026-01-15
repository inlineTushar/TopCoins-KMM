plugins {
    alias(libs.plugins.convention.multiplatform)
    alias(libs.plugins.convention.library.compose)
    alias(libs.plugins.convention.library.test)
    alias(libs.plugins.kotlin.serialization)
}

compose {
    resources {
        packageOfResClass = "com.tushar.common.navigation.generated.resources"
        generateResClass = always
    }
}

kotlin {
    androidLibrary {
        namespace = "com.tushar.common.navigation"
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.serialization.json)
            api(libs.compose.multiplatform.navigation)
            api(libs.compose.multiplatform.navigation.runtime)
        }
    }
}
