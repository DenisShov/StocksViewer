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

rootProject.name = "StocksViewer"

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
include(":core:testing")
include(":core:commonresources")
