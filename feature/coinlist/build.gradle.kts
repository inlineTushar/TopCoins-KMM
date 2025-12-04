plugins {
    alias(libs.plugins.local.library.feature)
}

android {
    namespace = "com.tushar.feature.coinlist"
}

compose {
    resources {
        packageOfResClass = "com.tushar.feature.coinlist.generated.resources"
        generateResClass = always
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                // Common modules
                implementation(project(":common:ui"))
                implementation(project(":common:data"))
                implementation(project(":common:domain"))
            }
        }
    }
}
