plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
}

kotlin {
    jvm()
    sourceSets {
        jvmMain.dependencies {
            implementation(project(":annotation"))
            implementation(libs.ksp.symbol.processing)
        }
    }
}

task("testClasses")
