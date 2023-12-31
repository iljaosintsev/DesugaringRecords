plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.turlir.records.android"
    compileSdk = 34 // minimum requirement for declaration records

    defaultConfig {
        applicationId = "com.turlir.records.android"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            signingConfig = signingConfigs["debug"]
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

val instrumentedClasspath: Configuration by configurations.creating {
    isCanBeConsumed = false
}

configurations.implementation.configure {
    extendsFrom(instrumentedClasspath)
}

dependencies {

    /*
    val exampleInstrumentedJars = mapOf(
        "path" to ":example",
        "configuration" to "instrumentedJars"
    )
    // compile error, it's ok by design R8
    instrumentedClasspath(project(exampleInstrumentedJars))
    */

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")

    testImplementation("junit:junit:4.13.2")
}