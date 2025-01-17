package config

import org.gradle.api.JavaVersion

object AppConfig {
    const val namespace = "com.nextxform.deeplinktester"
    const val compileSdk = 35
    const val applicationId = "com.nextxform.deeplinktester"
    const val minSdk = 21
    const val targetSdk = 35
    const val versionCode = 3
    const val versionName = "2.1"
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    val sourceCompatibility = JavaVersion.VERSION_17
    const val sourceTarget = "17"
}