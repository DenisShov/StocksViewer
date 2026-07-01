plugins {
    alias(libs.plugins.stockviewer.android.feature)
    alias(libs.plugins.stockviewer.android.library.compose)
    alias(libs.plugins.stockviewer.android.hilt)
    alias(libs.plugins.stockviewer.android.library.jacoco)
}

android {
    namespace = "com.feature.favorites.impl"
    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(project(":core:navigation"))
    implementation(project(":core:database"))
    implementation(project(":feature:favorites:api"))
    implementation(project(":feature:details:api"))
    implementation(project(":shared-library:favorites"))

    implementation(libs.androidx.compose.material3)
    implementation(libs.kotlinx.collections.immutable)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
}
