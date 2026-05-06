plugins {
    alias(libs.plugins.stockviewer.android.library)
    alias(libs.plugins.stockviewer.android.koin)
}

android {
    namespace = "com.sharedlibrary.favorites"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    api(project(":core:common"))
    implementation(project(":core:database"))

    testImplementation(project(":core:testing"))
}