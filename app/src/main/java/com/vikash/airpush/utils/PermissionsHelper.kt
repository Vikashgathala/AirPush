package com.vikash.airpush.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionsHelper {
    fun checkPermission(activity: Activity, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(activity: Activity, permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
    }

    fun arePermissionsGranted(grantResults: IntArray): Boolean {
        return grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }
    }
}
