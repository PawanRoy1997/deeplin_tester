plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("android-application") {
            id = "android-application"
            implementationClass = "plugins.AndroidApplication"
        }
    }
}