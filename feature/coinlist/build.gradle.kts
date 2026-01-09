plugins {
    alias(libs.plugins.convention.library.feature)
}

compose {
    resources {
        packageOfResClass = "com.tushar.feature.coinlist.generated.resources"
        generateResClass = always
    }
}

kotlin {
    androidLibrary {
        namespace = "com.tushar.feature.coinlist"

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
