package config

import org.gradle.api.JavaVersion

object AppConfig {
    const val namespace = "com.nextxform.deeplinktester"
    const val compileSdk = 34
    const val applicationId = "com.nextxform.deeplinktester"
    const val minSdk = 21
    const val targetSdk = 33
    const val versionCode = 1
    const val versionName = "1.0"
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    val sourceCompatibility = JavaVersion.VERSION_17
    val sourceTarget = "17"
}