plugins {
    alias(libs.plugins.codewars.android.library)
    alias(libs.plugins.codewars.android.hilt)
    alias(libs.plugins.codewars.android.library.jacoco)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.test.app.network"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

secrets {
    defaultPropertiesFileName = "secrets.defaults.properties"
}

dependencies {
    api(project(":core:common"))

    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.converter.gson)
    implementation(libs.timber)
    api(libs.arrow.core)
}
