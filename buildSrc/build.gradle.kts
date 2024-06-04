plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("ktlint") {
            id = "com.wsr.ktlint"
            implementationClass = "plugins.KtlintPlugin"
        }
    }
}
