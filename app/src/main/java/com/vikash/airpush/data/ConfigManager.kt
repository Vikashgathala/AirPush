package com.vikash.airpush.data

import android.content.Context
import android.content.SharedPreferences

object ConfigManager {
    private const val PREF_NAME = "airpush_prefs"
    private const val KEY_DEVICE_NAME = "device_name"
    private const val KEY_TRANSFER_PORT = "transfer_port"

    fun getDeviceName(context: Context): String {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_DEVICE_NAME, "AirPush Device") ?: "AirPush Device"
    }

    fun setDeviceName(context: Context, name: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_DEVICE_NAME, name).apply()
    }

    fun getTransferPort(context: Context): Int {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_TRANSFER_PORT, 8988)
    }

    fun setTransferPort(context: Context, port: Int) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_TRANSFER_PORT, port).apply()
    }
}