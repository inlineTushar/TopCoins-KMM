plugins {
    alias(libs.plugins.local.multiplatform)
}

android {
    namespace = "com.tushar.shared"
}

// This is an umbrella module that will contain converted KMM modules.
// No dependencies should be declared here.
// Those modules will declare their own dependencies

