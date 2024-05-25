plugins {
    alias(libs.plugins.codewars.android.feature)
    alias(libs.plugins.codewars.android.library.compose)
    alias(libs.plugins.codewars.android.library.jacoco)
}

android {
    namespace = "com.test.app.details"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}