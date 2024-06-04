plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
}

kotlin {
    jvm()
    sourceSets {
        jvmMain.dependencies {
            implementation(libs.navigatable.annotation)
            implementation(libs.ksp.symbol.processing)
        }
    }
}

task("testClasses")

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = libs.versions.navigatable.group.id.get()
            artifactId = "navigatable-processor"
            version = libs.versions.navigatable.version.get()
            from(components["kotlin"])
        }
    }
}
