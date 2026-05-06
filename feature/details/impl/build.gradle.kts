plugins {
    alias(libs.plugins.stockviewer.android.feature)
    alias(libs.plugins.stockviewer.android.koin)
    alias(libs.plugins.stockviewer.android.library.jacoco)
    alias(libs.plugins.stockviewer.android.library.compose)
}

android {
    namespace = "com.feature.details.impl"
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":feature:details:api"))
    implementation(project(":shared-library:favorites"))

    implementation(libs.timber)
    implementation(libs.vico.compose.m3)
    implementation(libs.coil)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
}