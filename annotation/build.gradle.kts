plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.serialization)
}

kotlin {
    applyDefaultHierarchyTemplate()

    jvm()
    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_1_8.toString()
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.serialization)
            }
        }
    }
}

android {
    namespace = "com.wsr.navigatable.annotation"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

task("testClasses")
