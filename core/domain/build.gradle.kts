plugins {
    alias(libs.plugins.codewars.android.library)
    alias(libs.plugins.codewars.android.hilt)
    alias(libs.plugins.codewars.android.library.jacoco)
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

    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}