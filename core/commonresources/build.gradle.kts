plugins {
    alias(libs.plugins.stockviewer.android.library)
}

android {
    namespace = "com.core.commonresources"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.hilt.android)
    implementation(libs.androidx.paging.compose)
    implementation(libs.timber)

    testImplementation(project(":core:testing"))
}
