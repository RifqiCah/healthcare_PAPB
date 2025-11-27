// Top-level build file
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Hilt plugin
    id("com.google.dagger.hilt.android") version "2.51.1" apply false

    // Google Services
    id("com.google.gms.google-services") version "4.4.2" apply false

    // KSP
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
}
