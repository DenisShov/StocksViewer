plugins {
    alias(libs.plugins.stockviewer.android.library)
    alias(libs.plugins.stockviewer.android.hilt)
    alias(libs.plugins.stockviewer.android.library.jacoco)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.core.domain"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    api(project(":core:model"))
    api(project(":core:common"))
    api(project(":core:network"))

    testImplementation(project(":core:testing"))
}