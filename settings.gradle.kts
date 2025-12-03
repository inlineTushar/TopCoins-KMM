pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
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

// Common modules
include(":common:data")
include(":common:domain")
include(":common:navigation")
include(":common:ui")

// Feature modules
include(":feature:coinlist")
