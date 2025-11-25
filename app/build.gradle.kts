plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // Terapkan plugin Compose Compiler, Hilt, dan Kapt
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")

    // Terapkan plugin Firebase Google Services
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

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Menggunakan API Key dari kode lama
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

    // Konfigurasi Compose Compiler
    composeOptions {
        // Menggunakan versi yang lebih stabil (1.5.10 dari kode Anda)
        kotlinCompilerExtensionVersion = "1.5.10"
    }
}

dependencies {
    // --- Default Libraries ---
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    // Menggunakan string manual untuk UI/Graphics/Preview untuk konsistensi dengan kode Anda
    implementation(libs.androidx.ui)
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")

    implementation(libs.androidx.material3)
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation(libs.androidx.foundation)

    // --- Firebase ---
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    // --- Google Sign In SDK (PENTING untuk LoginScreen) ---
    implementation("com.google.android.gms:play-services-auth:21.0.0")

    // --- Retrofit & Network ---
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // --- Coroutines ---
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // --- HILT DEPENDENCIES ---
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // --- Library Tambahan (Coil) ---
    implementation("io.coil-kt:coil-compose:2.5.0")

    // --- Testing ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

kapt {
    correctErrorTypes = true
}