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

rootProject.name = "GestorTurnosLaundry"
include(":app")
include(":core")
include(":data")
include(":domain")
include(":admin-ui")
include(":user-ui")
include(":sensors-api")