import config.Deps
import config.AppConfig

plugins {
    alias(libs.plugins.application)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kspPlugin)
}

android {
    namespace = AppConfig.namespace
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = AppConfig.testInstrumentationRunner
        vectorDrawables {
            useSupportLibrary = true
        }

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = AppConfig.sourceCompatibility
        targetCompatibility = AppConfig.sourceCompatibility
    }
    kotlinOptions {
        jvmTarget = AppConfig.sourceTarget
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(platform(Deps.Compose.composeBom))
    androidTestImplementation(platform(Deps.Compose.composeBom))

    Deps.Core.list.forEach(::implementation)

    Deps.Compose.list.forEach(::implementation)
    implementation(Deps.Room.room)
    ksp(Deps.Room.room_ksp)

    Deps.LifeCycle.list.forEach(::implementation)

    Deps.Testing.unitTest.forEach(::testImplementation)
    Deps.Testing.androidTest.forEach(::androidTestImplementation)
    Deps.Debug.list.forEach(::debugImplementation)
}