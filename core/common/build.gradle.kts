plugins {
    alias(libs.plugins.codewars.android.library)
    alias(libs.plugins.codewars.android.hilt)
    alias(libs.plugins.codewars.android.library.jacoco)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.test.app.common"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.converter.gson)
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.navigation3.runtime)
}