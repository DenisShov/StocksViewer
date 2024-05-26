pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CodeWars"

include(":app")
include(":core:network")
include(":core:data")
include(":core:model")
include(":core:domain")
include(":core:designsystem")
include(":core:common")
include(":core:ui")
include(":feature:list")
include(":feature:details")
