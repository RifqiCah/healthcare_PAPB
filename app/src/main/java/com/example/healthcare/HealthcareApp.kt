package com.example.healthcare

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp // <--- INI SAKELAR UTAMA BIAR HILT NYALA
class HealthcareApp : Application()