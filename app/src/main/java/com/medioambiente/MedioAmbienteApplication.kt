package com.medioambiente

import android.app.Application
import com.medioambiente.network.ApiClient

class MedioAmbienteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ApiClient.initialize(this)
    }
}