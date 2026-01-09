plugins {
    alias(libs.plugins.convention.multiplatform)
    alias(libs.plugins.convention.library.koin)
    alias(libs.plugins.convention.library.test)
}

kotlin {
    androidLibrary {
        namespace = "com.tushar.common.core"
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.datetime)
        }
    }
}
