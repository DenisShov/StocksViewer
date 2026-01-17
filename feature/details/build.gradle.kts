plugins {
    alias(libs.plugins.codewars.android.feature)
    alias(libs.plugins.codewars.android.hilt)
    alias(libs.plugins.codewars.android.library.jacoco)
    alias(libs.plugins.codewars.android.library.compose)
}

android {
    namespace = "com.test.app.details"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:ui"))
    implementation(libs.timber)
    implementation(libs.vico.compose.m3)
    implementation(libs.coil)
    implementation(libs.material.icons)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
}