package config

object Deps {
    object Core {
        const val core = ("androidx.core:core-ktx:1.12.0")
        const val activity = ("androidx.activity:activity-compose:1.8.1")
        val list = listOf(core, activity)
    }

    object Compose {
        const val composeBom = "androidx.compose:compose-bom:2023.03.00"
        const val ui = "androidx.compose.ui:ui"
        const val graphics = "androidx.compose.ui:ui-graphics"
        const val tooling_preview = "androidx.compose.ui:ui-tooling-preview"
        const val material3 = "androidx.compose.material3:material3"
        val list = listOf(ui, graphics, tooling_preview, material3)
    }

    object Room {
        private const val roomVersion = "2.6.0"
        const val room = "androidx.room:room-ktx:$roomVersion"
        const val room_ksp = "androidx.room:room-compiler:$roomVersion"
    }

    object LifeCycle {
        private const val lifeCycleVersion = "2.6.2"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifeCycleVersion"
        const val viewModelCompose =
            "androidx.lifecycle:lifecycle-viewmodel-compose:$lifeCycleVersion"
        const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:$lifeCycleVersion"
        const val runtimeCompose = "androidx.lifecycle:lifecycle-runtime-compose:$lifeCycleVersion"
        val list = listOf(viewModel, viewModelCompose, lifecycleRuntime, runtimeCompose)
    }

    object Testing {
        const val junit = "junit:junit:4.13.2"
        const val junitExt = "androidx.test.ext:junit:1.1.5"
        const val espressoCore = "androidx.test.espresso:espresso-core:3.5.1"
        const val junitUi = "androidx.compose.ui:ui-test-junit4"
        val unitTest = listOf(junit)
        val androidTest = listOf(junitExt, espressoCore, junitUi)
    }

    object Debug {
        const val uiTooling = "androidx.compose.ui:ui-tooling"
        const val uiTestManifest = "androidx.compose.ui:ui-test-manifest"
        val list = listOf(uiTooling, uiTestManifest)
    }
}