plugins {
    alias(libs.plugins.stockviewer.android.feature)
    alias(libs.plugins.stockviewer.android.library.compose)
    alias(libs.plugins.stockviewer.android.koin)
    alias(libs.plugins.stockviewer.android.library.jacoco)
}

android {
    namespace = "com.feature.list.impl"
    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":feature:list:api"))
    implementation(project(":feature:details:api"))

    implementation(libs.androidx.paging.compose)
    implementation(libs.coil)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
}