plugins {
    alias(libs.plugins.codewars.android.library)
    alias(libs.plugins.codewars.android.library.compose)
    alias(libs.plugins.codewars.android.library.jacoco)
}

android {
    namespace = "com.test.app.designsystem"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.material.icons)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui.util)
}