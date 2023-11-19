package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplication : Plugin<Project> {
    companion object {
        const val ANDROID = "com.android.application"
        const val KOTLIN_ANDROID = "org.jetbrains.kotlin.android"
        const val KOTLIN_KSP = "com.google.devtools.ksp"
    }

    override fun apply(target: Project) {
        target.plugins.apply("com.android.application")
        target.plugins.apply("org.jetbrains.kotlin.android")
        target.plugins.apply("com.google.devtools.ksp")
    }
}