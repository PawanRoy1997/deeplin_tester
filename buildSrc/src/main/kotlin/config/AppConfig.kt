package config

import org.gradle.api.JavaVersion

object AppConfig {
    const val NAMESPACE = "com.nextxform.deeplinktester"
    const val COMPILE_SDK = 36
    const val APPLICATION_ID = "com.nextxform.deeplinktester"
    const val MIN_SDK = 23
    const val TARGET_SDK = 36
    const val VERSION_CODE = 3
    const val VERSION_NAME = "2.1"
    const val TEST_INSTRUMENTATION_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
    val SOURCE_COMPATIBILITY = JavaVersion.VERSION_17
}