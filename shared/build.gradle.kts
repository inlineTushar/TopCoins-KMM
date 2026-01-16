plugins {
    alias(libs.plugins.convention.multiplatform)
    alias(libs.plugins.convention.library.compose)
    alias(libs.plugins.touchlab.skie)
}


compose {
    resources {
        packageOfResClass = "com.tushar.shared.resources"
        generateResClass = always
    }
}

android {
    namespace = "com.tushar.shared"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.touchlab.skie.annotations)
            api(project(":common:core"))
            api(project(":common:ui"))
            api(project(":common:data"))
            api(project(":common:domain"))
            api(project(":common:navigation"))

            api(project(":feature:coinlist"))
            api(project(":feature:priceupdate"))
        }
    }

    // Configure iOS framework export
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries.withType<org.jetbrains.kotlin.gradle.plugin.mpp.Framework> {
            // Export all dependencies to make them available to iOS
            export(project(":feature:coinlist"))
            export(project(":feature:priceupdate"))
            export(project(":common:core"))
            export(project(":common:ui"))
            export(project(":common:data"))
            export(project(":common:domain"))
        }
    }

    task("testClasses")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
