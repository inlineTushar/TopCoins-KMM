pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Top Coins"

include(":app")

// Shared KMM module
include(":shared")

// Common modules
include(":common:core")
include(":common:ui")
include(":common:data")
include(":common:domain")
include(":common:navigation")

// Feature modules
include(":feature:coinlist")
include(":feature:priceupdate")