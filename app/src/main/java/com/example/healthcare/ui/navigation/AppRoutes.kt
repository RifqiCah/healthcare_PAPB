// Di dalam folder /ui/navigation/AppRoutes.kt
package com.example.healthcare.ui.navigation

object AppRoutes {
    //Rute untuk profile
    const val PROFILE_SCREEN = "profile"


    // Rute untuk otentikasi
    const val LOGIN_SCREEN = "login"
    const val REGISTER_SCREEN = "register"

    // Rute utama setelah login
    const val HOME_SCREEN = "home"
    const val ARTIKEL_SCREEN = "artikel"

    // Rute untuk Sistem Pakar
    const val SISTEM_PAKAR_SCREEN = "sistem_pakar"
    const val GEJALA_SCREEN = "gejala"
    const val KONDISI_SCREEN = "kondisi"


    // --- Rute Detail Sistem Pakar (BARU) ---
    const val PAKAR_DETAIL_ROUTE = "pakar_detail"
    const val PAKAR_DETAIL_ARG_ID = "pakarId"
    // Rute lengkap: "pakar_detail/{pakarId}"
    const val PAKAR_DETAIL_SCREEN = "$PAKAR_DETAIL_ROUTE/{$PAKAR_DETAIL_ARG_ID}"

    // --- Rute Detail Artikel (BARU) ---
    const val ARTIKEL_DETAIL_ROUTE = "artikel_detail"
    const val ARTIKEL_DETAIL_ARG_ID = "artikelId"
    // Rute lengkap: "artikel_detail/{artikelId}"
    const val ARTIKEL_DETAIL_SCREEN = "$ARTIKEL_DETAIL_ROUTE/{$ARTIKEL_DETAIL_ARG_ID}"

    const val FORGOT_PASSWORD_SCREEN = "forgot_password"

}