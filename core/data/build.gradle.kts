plugins {
    alias(libs.plugins.stockviewer.android.library)
    alias(libs.plugins.stockviewer.android.hilt)
    alias(libs.plugins.stockviewer.android.library.jacoco)
}

android {
    namespace = "com.test.app.data"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    api(project(":core:model"))
    api(project(":core:network"))

    implementation(libs.androidx.paging.compose)
    implementation(libs.joda.time)
    implementation(libs.timber)

    testImplementation(project(":core:testing"))
}