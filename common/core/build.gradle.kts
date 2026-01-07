plugins {
    alias(libs.plugins.convention.multiplatform)
    alias(libs.plugins.convention.library.koin)
    alias(libs.plugins.convention.library.test)
}

android {
    namespace = "com.tushar.common.core"
}

dependencies {
    api(libs.kotlinx.datetime)
}
