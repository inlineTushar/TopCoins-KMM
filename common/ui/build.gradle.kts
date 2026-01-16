plugins {
    alias(libs.plugins.convention.multiplatform)
    alias(libs.plugins.convention.library.compose)
    alias(libs.plugins.convention.library.koin)
    alias(libs.plugins.convention.library.test)
}

android {
    namespace = "com.tushar.common.ui"
}

compose {
    resources {
        packageOfResClass = "com.tushar.common.ui.resources"
        generateResClass = always
    }
}
