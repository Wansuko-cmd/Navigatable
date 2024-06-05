// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.multiplatform) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("maven-publish")
}

subprojects {
    apply(plugin = "maven-publish")
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

val ktlint by configurations.creating
dependencies {

    // Version Catalogに登録不可
    ktlint("com.pinterest:ktlint:0.49.1") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
}

tasks.register<JavaExec>("ktlintCheck") {
    group = "ktlint"
    description = "Check Kotlin code style."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args = listOf("src/**/*.kt", "**.kts", "!**/build/**")
}

tasks.register<JavaExec>("ktlintFormat") {
    group = "ktlint"
    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    jvmArgs = listOf("--add-opens=java.base/java.lang=ALL-UNNAMED")
    args = listOf("-F", "src/**/*.kt", "**.kts", "!**/build/**")
}
