plugins {
    alias(libs.plugins.stockviewer.android.feature)
    alias(libs.plugins.stockviewer.android.hilt)
    alias(libs.plugins.stockviewer.android.library.jacoco)
    alias(libs.plugins.stockviewer.android.library.compose)
}

android {
    namespace = "com.test.app.stockviewer.feature.details.impl"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":feature:details:api"))

    implementation(libs.timber)
    implementation(libs.vico.compose.m3)
    implementation(libs.coil)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
}