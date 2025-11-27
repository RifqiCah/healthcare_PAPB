plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // Compose Compiler
    id("org.jetbrains.kotlin.plugin.compose")

    // KSP
    id("com.google.devtools.ksp")

    // Hilt
    id("com.google.dagger.hilt.android")

    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.healthcare"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.healthcare"
        minSdk = 24
        targetSdk = 36

        versionCode = 1
        versionName = "1.0"

        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "NEWS_API_KEY", "\"pub_3d90d6e66e784e2bb222e1ce7a92712d\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // ========================
    // ⭐ HILT (wajib)
    // ========================
    implementation("com.google.dagger:hilt-android:2.51.1")
    implementation(libs.androidx.animation)
    ksp("com.google.dagger:hilt-compiler:2.51.1")

    // ========================
    // ⭐ Retrofit & OkHttp
    // ========================
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // ========================
    // AndroidX Core
    // ========================
    implementation("androidx.core:core-ktx:1.17.0")

    implementation("androidx.multidex:multidex:2.0.1")

    // Compose
    implementation("androidx.activity:activity-compose:1.11.0")

    implementation(platform("androidx.compose:compose-bom:2024.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.2")

    // Hilt Navigation
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.4")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")

    // Coil
    implementation("io.coil-kt:coil-compose:2.7.0")

    // DataStore Preferences
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.datastore:datastore-core:1.1.1")

    // Material Icons Compose
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")

    implementation("com.google.android.gms:play-services-auth:21.2.0")

    implementation("com.google.firebase:firebase-storage")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")


}
