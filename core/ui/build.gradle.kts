plugins {
    alias(libs.plugins.stockviewer.android.library)
    alias(libs.plugins.stockviewer.android.library.compose)
    alias(libs.plugins.stockviewer.android.library.jacoco)
}

android {
    namespace = "com.test.app.ui"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:model"))

    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.util)
}