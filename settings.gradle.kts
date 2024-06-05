@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url = "https://androidx.dev/storage/compose-compiler/repository/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url = "https://androidx.dev/storage/compose-compiler/repository/")
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "Navigatable"

include(":annotation")
include(":processor")

include(":sample:androidApp")
