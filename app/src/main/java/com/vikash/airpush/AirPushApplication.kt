package com.vikash.airpush

import android.app.Application
import android.util.Log

typealias AirPushLogger = (String) -> Unit

class AirPushApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeLogging()
        Log.d("AirPushApplication", "App Started")
    }

    private fun initializeLogging() {
        // Initialize logging system if required
    }
}
