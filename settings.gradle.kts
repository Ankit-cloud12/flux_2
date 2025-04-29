pluginManagement {
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

rootProject.name = "RankerZ"
include(":app")
include(":feature:brightness")
include(":feature:temperature")
include(":feature:scheduling")
include(":feature:settings")
include(":feature:profiles")
include(":feature:perapp")
include(":core:ui")
include(":core:domain")
include(":core:data")
include(":core:common")
include(":data:local")
include(":data:system")