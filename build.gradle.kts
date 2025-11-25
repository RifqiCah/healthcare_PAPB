plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Pastikan ini ada dan versinya cocok dengan dependensi Hilt (2.48)
    id("com.google.dagger.hilt.android") version "2.48" apply false

    // Plugin Google Services harus dideklarasikan di sini
    id("com.google.gms.google-services") version "4.4.2" apply false
}