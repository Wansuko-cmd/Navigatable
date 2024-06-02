plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.serialization)
}

kotlin {
    jvm()
    sourceSets {
        commonMain.dependencies {
            api(libs.kotlin.serialization)
        }

        jvmMain.dependencies {
            implementation(libs.ksp.symbol.processing)
        }
    }
}

task("testClasses")
