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
include(":core:designsystem")
include(":core:common")
include(":core:ui")
include(":core:testing")
include(":core:commonresources")
include(":core:navigation")
include(":core:database")
include(":core:domain")
include(":feature:list:api")
include(":feature:list:impl")
include(":feature:details:api")
include(":feature:details:impl")
include(":feature:favorites:api")
include(":feature:favorites:impl")
