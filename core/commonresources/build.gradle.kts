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
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.androidx.paging.compose)
    implementation(libs.timber)

    testImplementation(project(":core:testing"))
}
