// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.multiplatform) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("maven-publish")
}

buildscript {
    dependencies {
        classpath(libs.gradle.android)
    }
}

subprojects {
    apply(plugin = "maven-publish")
    apply(plugin = Plugins.ktlint)
}

publishing {
    publications {
        create<MavenPublication>("processor") {
            groupId = libs.versions.navigatable.group.id.get()
            artifactId = "navigatable-processor"
            version = libs.versions.navigatable.version.get()
        }

        create<MavenPublication>("annotation") {
            groupId = libs.versions.navigatable.group.id.get()
            artifactId = "navigatable-annotation"
            version = libs.versions.navigatable.version.get()
        }
    }
}
