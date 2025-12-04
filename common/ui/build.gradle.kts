plugins {
    alias(libs.plugins.local.multiplatform)
    alias(libs.plugins.local.library.composeview)
    alias(libs.plugins.local.library.koin)
    alias(libs.plugins.local.library.test)
}

android {
    namespace = "com.tushar.ui"
}

compose {
    resources {
        packageOfResClass = "com.tushar.ui.resources"
        generateResClass = always
    }
}
