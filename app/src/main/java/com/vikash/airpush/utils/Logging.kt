package com.vikash.airpush.utils

import android.util.Log

object Logging {
    private const val TAG = "AirPush"

    fun debug(message: String) {
        Log.d(TAG, message)
    }

    fun error(message: String, throwable: Throwable? = null) {
        Log.e(TAG, message, throwable)
    }

    fun info(message: String) {
        Log.i(TAG, message)
    }
}