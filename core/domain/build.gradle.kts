plugins {
    alias(libs.plugins.codewars.android.library)
    alias(libs.plugins.codewars.android.library.jacoco)
}

android {
    namespace = "com.test.app.domain"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}