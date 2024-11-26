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

rootProject.name = "CG_lab2"
include(":app")
include(":paintFeature")
include(":fractalBuilderFeature")
include(":ImageProcessingFeature")
include(":projecting_feature")
