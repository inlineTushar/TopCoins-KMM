plugins {
    alias(libs.plugins.local.library)
    alias(libs.plugins.local.library.koin)
    alias(libs.plugins.local.library.test)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.tushar.domain"
}

dependencies {
    // Domain should not depend on data layer
}
