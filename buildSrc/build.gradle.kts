plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("ktlint") {
            id = "com.wsr.ktlint"
            implementationClass = "plugins.KtlintPlugin"
        }
        register("publish") {
            id = "com.wsr.publish"
            implementationClass = "plugins.PublishPlugin"
        }
    }
}
