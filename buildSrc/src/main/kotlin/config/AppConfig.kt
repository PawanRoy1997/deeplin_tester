package config

import org.gradle.api.JavaVersion

object AppConfig {
    const val NAMESPACE = "com.nextxform.deeplinktester"
    const val COMPILE_SDK = 37
    const val APPLICATION_ID = "com.nextxform.deeplinktester"
    const val MIN_SDK = 23
    const val TARGET_SDK = 37
    const val VERSION_CODE = 6
    const val VERSION_NAME = "4.2"
    const val TEST_INSTRUMENTATION_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
    val SOURCE_COMPATIBILITY = JavaVersion.VERSION_17
}