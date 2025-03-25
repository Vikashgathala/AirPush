package com.vikash.airpush.transfer

import android.util.Log
import java.net.NetworkInterface
import java.net.Socket
import java.util.Collections

object AirDropUtils {
    fun getLocalIpAddress(): String? {
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (networkInterface in interfaces) {
                val addresses = Collections.list(networkInterface.inetAddresses)
                for (address in addresses) {
                    if (!address.isLoopbackAddress) {
                        return address.hostAddress
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("AirDropUtils", "Error getting local IP address", e)
        }
        return null
    }

    fun isDeviceReachable(targetIp: String, timeout: Int = 2000): Boolean {
        return try {
            val socket = Socket(targetIp, 8988)
            socket.close()
            true
        } catch (e: Exception) {
            false
        }
    }
}
