plugins {
    alias(libs.plugins.convention.library.feature)
}

android {
    namespace = "com.tushar.feature.priceupdate"
}

compose {
    resources {
        packageOfResClass = "com.tushar.feature.priceupdate.generated.resources"
        generateResClass = always
    }
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
