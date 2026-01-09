plugins {
    alias(libs.plugins.convention.library.feature)
}

compose {
    resources {
        packageOfResClass = "com.tushar.feature.priceupdate.generated.resources"
        generateResClass = always
    }
}

kotlin {
    androidLibrary {
        namespace = "com.tushar.feature.priceupdate"

        androidResources {
            enable = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":common:core"))
            implementation(project(":common:ui"))
            implementation(project(":common:data"))
            implementation(project(":common:domain"))
        }
    }
}
