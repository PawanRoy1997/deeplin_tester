import com.android.build.gradle.internal.utils.isKotlinAndroidPluginApplied

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.application) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.kspPlugin) apply false
}