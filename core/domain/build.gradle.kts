plugins {
    alias(libs.plugins.codewars.android.library)
    alias(libs.plugins.codewars.android.hilt)
    alias(libs.plugins.codewars.android.library.jacoco)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.test.app.domain"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    api(project(":core:data"))
    api(project(":core:model"))
    api(project(":core:common"))

    testImplementation(project(":core:testing"))
}