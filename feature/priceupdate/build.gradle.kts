plugins {
    alias(libs.plugins.convention.library.feature)
}

compose {
    resources {
        packageOfResClass = "com.tushar.feature.priceupdate.generated.resources"
        generateResClass = always
    }
}

android {
    namespace = "com.tushar.feature.priceupdate"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":common:core"))
            implementation(project(":common:ui"))
            implementation(project(":common:data"))
            implementation(project(":common:domain"))
        }
    }
}
